
const getDateTime = function (dateString) {
    let now = new Date();
    let hour = parseInt(dateString.substr(0, 2));
    let minute = parseInt(dateString.substr(3, 2));
    let second = parseInt(dateString.substr(6, 2));

    return new Date(now.getFullYear(), now.getMonth(), now.getDate(), hour, minute, second, 0);
};
function packetParser(line) {
    let packetMeta = {};


    line = line.trim();

    let currIndex = line.indexOf(' ');

    packetMeta.dateTime = getDateTime(line.substr(0, currIndex));
    currIndex = line.indexOf(' ', currIndex + 1);

    packetMeta.sourceAddr = line.substr(currIndex, line.indexOf('>') - currIndex).trim();
    packetMeta.sourcePort = packetMeta.sourceAddr.substr(packetMeta.sourceAddr.lastIndexOf('.') + 1);
    packetMeta.sourceAddr = packetMeta.sourceAddr.substr(0, packetMeta.sourceAddr.lastIndexOf('.')).trim();

    currIndex = line.indexOf('>', currIndex) + 1;

    packetMeta.destAddr = line.substr(currIndex, line.indexOf(' ', currIndex + 1) - currIndex);
    packetMeta.destPort = packetMeta.destAddr.substr(packetMeta.destAddr.lastIndexOf('.') + 1);
    packetMeta.destPort = packetMeta.destPort.substr(0, packetMeta.destPort.length - 1);
    packetMeta.destAddr = packetMeta.destAddr.substr(0, packetMeta.destAddr.lastIndexOf('.')).trim();

    packetMeta.size = parseInt(line.substr(line.indexOf(' ', line.indexOf('length'))).trim());

    return packetMeta;

}

module.exports = packetParser;