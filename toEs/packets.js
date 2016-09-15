var events = require('events');
var Packet = require('./packet');

function Packets() {
    events.EventEmitter.call(this);
    var packet = {};
    this.addPacket = function (line) {
        //console.log(line);

        if (line.indexOf('length') != -1 && line.indexOf(' IP ') != -1) {
            this.packet = new Packet(line);
            //console.log('PACKET WAS ADDED');
            this.emit('packetAdded');
        } else {
            //console.log('PACKET WAS NOT ADDED');
        }
    };


}
Packets.prototype.__proto__ = events.EventEmitter.prototype;

module.exports = Packets;