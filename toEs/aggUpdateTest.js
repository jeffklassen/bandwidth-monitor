var client = require('./esclient');
var Aggregation = require('./aggregation').Aggregation;
var AggregationType = require('./aggregation').AggregationType;


var hourAggregation = new Aggregation(AggregationType.HOUR);

function indexAggregation() {
    console.warn(this);
    //client.indexAggregation(this);
    client.updateAggregation(hourAggregation);
}

hourAggregation.on('aggregationRolledOver', indexAggregation)



function addMinutes(date, minutes) {
    return new Date(date.getTime() + minutes * 60000);
}

hourAggregation.addPacket({
    dateTime: new Date(),
    sourceAddr: "us.archive.ubuntu.com",
    sourcePort: "http",
    destAddr: "192.168.1.21",
    destPort: "47548",
    size: 1
});

hourAggregation.addPacket({
    dateTime: addMinutes(new Date(), 4),
    sourceAddr: "192.168.1.21",
    sourcePort: "http",
    destAddr: "yahoo.com",
    destPort: "47548",
    size: 1
});


hourAggregation.addPacket({
    dateTime: addMinutes(new Date(), 5),
    sourceAddr: "192.168.1.21",
    sourcePort: "http",
    destAddr: "yahoo.com",
    destPort: "47548",
    size: 1
});
client.updateAggregation(hourAggregation);
console.log(hourAggregation);

hourAggregation.addPacket({
    dateTime: addMinutes(new Date(), 61),
    sourceAddr: "192.168.1.21",
    sourcePort: "http",
    destAddr: "yahoo.com",
    destPort: "47548",
    size: 1
});
client.updateAggregation(hourAggregation);

console.log(hourAggregation);