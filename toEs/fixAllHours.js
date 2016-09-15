var esclient = require('./esclient');
var async = require('async');

var Aggregation = require('./aggregation').Aggregation;
var AggregationType = require('./aggregation').AggregationType;
Date.prototype.addHours = function(h) {
    this.setHours(this.getHours() + h);
    return this;
};

esclient.earliestPacket(function(date) {
    var now = new Date();
    now.setMinutes(0);
    now.setSeconds(0);
    now.setMilliseconds(0);
    
    now = new Date(now.getTime()-1);
    console.log("Start", date);
    
    var hours = [];
    while (date < now) {
        var start = new Date(date.getTime());
        start.setMinutes(0);
        start.setSeconds(0);
        start.setMilliseconds(0);

        var end = new Date(date.getTime());

        end.setMinutes(59);
        end.setSeconds(59);
        end.setMilliseconds(999);
          
        hours.push({ start: start, end: end });
        date.addHours(1);
    }
    async.forEachLimit(hours, 2, function (hour, c) {
        esclient.aggregateCurrentHour(hour.start, hour.end, function (updown) {
            console.log(updown);

            var hourAggregation = new Aggregation(AggregationType.HOUR);
            hourAggregation.down = updown.down;
            hourAggregation.up = updown.up;
            hourAggregation.dateTime = updown.start;

            esclient.indexAggregation(hourAggregation);
            c();
        })
    });
});