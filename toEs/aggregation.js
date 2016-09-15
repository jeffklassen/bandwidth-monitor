"use strict";
var events = require('events');

// the first 3 octets of the local subnet
var subnet = '192.168.1';

// We aggregate packets by both second and hour. These are packet collections for these time periods
var AggregationType = {
    SECOND: {
        name: 'second',
        dateTimeComparer: function (packetDateTime, objectDateTime) {
            if (packetDateTime.getSeconds() === objectDateTime.getSeconds()) {
                return true;
            } else {
                return false;
            }
        },

        getDateTime: function (packetDateTime) {
            return packetDateTime;
        }
    },
    HOUR: {
        name: 'hour',
        dateTimeComparer: function (packetDateTime, objectDateTime) {
            //if (packetDateTime.getMinutes() === objectDateTime.getMinutes()) {
            if (packetDateTime.getHours() === objectDateTime.getHours()) {
                return true;
            } else {
                return false;
            }
        },
        // we want the time to the most recent hour
        getDateTime: function (packetDateTime) {
            var returnDateTime = packetDateTime;
            returnDateTime.setMinutes(0);
            returnDateTime.setSeconds(0);
            returnDateTime.setMilliseconds(0);
            return returnDateTime;
        }
    }
};

var Aggregation = function (aggregationType) {
    events.EventEmitter.call(this);
    if (aggregationType) {
        this.aggregationType = aggregationType;
    } else {
        this.aggregationType = AggregationType.SECOND;
    }

    this.resetAggregation();

};
Aggregation.prototype.packets = [];

Aggregation.prototype.resetAggregation = function (packet) {
    if (packet) {
        this.dateTime = this.aggregationType.getDateTime(packet.dateTime);
    } else {
        this.dateTime = this.aggregationType.getDateTime(new Date());
    }
    this.up = 0;
    this.down = 0;
    this.packets = [];
};

// add a packet to the aggregation
Aggregation.prototype.addPacket = function (packet) {
    if (this.dateTime) {
        // we need to detect to see if the aggregation has rolled over.
        if (this.aggregationType.dateTimeComparer(packet.dateTime, this.dateTime)) {
            if (this.aggregationType.name === "second") {
                this.packets.push(packet);
            }

            //if the source address is on the local subnet, then its an upload. Otherwise its a download
            if (packet.sourceAddr.indexOf(subnet) !== -1) {
                this.up += packet.size;
            } else {
                //download = true;
                this.down += packet.size;
            }
        } else {
            console.log("old time: " + this.dateTime);
            this.emit('aggregationRolledOver');

            this.resetAggregation(packet);
            this.addPacket(packet);
        }
    } else {
        this.addPacket(packet);
    }

};
Aggregation.prototype.__proto__ = events.EventEmitter.prototype;
module.exports.AggregationType = AggregationType;
module.exports.Aggregation = Aggregation;