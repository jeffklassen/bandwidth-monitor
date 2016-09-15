'use strict';

// indexing belongs in subprocess
var cp = require('child_process');
var esChild = cp.fork(__dirname + '/esclient');

// packet metadata parsing
var Packets = require('./packets');
var packets = new Packets();

// aggregate into different time denominations
var Aggregation = require('./aggregation').Aggregation;
var AggregationType = require('./aggregation').AggregationType;

var secondAggregation = new Aggregation(AggregationType.SECOND);
var hourAggregation = new Aggregation(AggregationType.HOUR);

var end = new Date();
var start = new Date();

start.setMinutes(0);
start.setSeconds(0);
start.setMilliseconds(0);

require('./esclient').aggregateCurrentHour(start, end, function(updown) {
    hourAggregation.down = updown.down;
    hourAggregation.up = updown.up;
    console.log(updown);
})


// occasionally an incorrect date comes through that is impossibly 
// in the past or future. Datacleaner handles that and needs to be run every minute
var datacleaner = require('./cleandata');
//setInterval(datacleaner, 60000);


// collect input from TCP dump
var spawn = require('child_process').spawn;
var tcpdump = spawn('tcpdump', ['-i', 'eth1']);

//handles splitting buffers by tokens
var StreamSplitter = require("stream-splitter");

// configure the buffer splitter to split on new lines
var splitter = tcpdump.stdout.pipe(StreamSplitter("\n"));
splitter.encoding = "utf8";

splitter.on("token", function(token) {
    //console.log("A line of input:", token);
    packets.addPacket(token);

});

function updateAggregations() {
    secondAggregation.addPacket(this.packet);
    hourAggregation.addPacket(this.packet);
};

function indexAggregation() {
    //var secondAggCopy = JSON.parse(JSON.stringify(this));
    //var hourAggCopy = JSON.parse(JSON.stringify(hourAggregation))

    console.warn('*****');
    //console.warn("second", secondAggCopy.dateTime, secondAggCopy.up, secondAggCopy.down);
    esChild.send({
        aggregation: this
    });
    //client.indexAggregation(secondAggCopy);

    console.warn('*****');
    //console.warn("hour", hourAggCopy.dateTime, hourAggCopy.up, hourAggCopy.down);
    console.warn('*****');

    esChild.send({
        aggregation: hourAggregation
    });
    //client.indexAggregation(hourAggCopy);
}


packets.on('packetAdded', updateAggregations);
secondAggregation.on('aggregationRolledOver', indexAggregation)
