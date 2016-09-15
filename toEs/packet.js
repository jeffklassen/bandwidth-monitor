function Packet(line) {

    var getDateTime = function (dateString) {
        var now = new Date();
        var hour = parseInt(dateString.substr(0, 2));
        var minute = parseInt(dateString.substr(3, 2));
        var second = parseInt(dateString.substr(6, 2));
        
        return new Date(now.getFullYear(), now.getMonth(), now.getDate(), hour, minute, second, 0);
    };

    line = line.trim();

    var currIndex = line.indexOf(' ');

    this.dateTime = getDateTime(line.substr(0, currIndex));
    currIndex = line.indexOf(' ', currIndex + 1);

    this.sourceAddr = line.substr(currIndex, line.indexOf('>') - currIndex).trim();
    this.sourcePort = this.sourceAddr.substr(this.sourceAddr.lastIndexOf('.') + 1);
    this.sourceAddr = this.sourceAddr.substr(0, this.sourceAddr.lastIndexOf('.')).trim();

    currIndex = line.indexOf('>', currIndex) + 1;

    this.destAddr = line.substr(currIndex, line.indexOf(' ', currIndex + 1) - currIndex);
    this.destPort = this.destAddr.substr(this.destAddr.lastIndexOf('.') + 1);
    this.destPort = this.destPort.substr(0, this.destPort.length - 1);
    this.destAddr = this.destAddr.substr(0, this.destAddr.lastIndexOf('.')).trim();

    this.size = parseInt(line.substr(line.lastIndexOf(' ')).trim());
}

module.exports = Packet;