var client = require('./esclient');
var AggregationType = require('./aggregation').AggregationType;

module.exports = function () {
    client.deleteFuture(AggregationType.SECOND, function (data) {
        console.log(data)
    });

    client.deleteFuture(AggregationType.HOUR, function (data) {
        console.log(data)
    });

    client.deleteNulls(AggregationType.SECOND, function (data) {
        console.log(data)
    });
    client.deleteNulls(AggregationType.HOUR, function (data) {
        console.log(data)
    });
};