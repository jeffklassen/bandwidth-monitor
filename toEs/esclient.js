var elasticsearch = require('elasticsearch');


process.on('message', function (msg) {
    if (msg.aggregation) {
        indexAggregation(msg.aggregation);
    }
});

const hosts = [
    process.env.ES1,
    process.env.ES2,
    process.env.ES3,
    process.env.ES4
];
console.log('HOSTSHOSTSHOSTSHOSTSHOSTSHOSTS', hosts);
function getClient() {

    // Connect the client to the following nodes, requests will be
    // load-balanced between them using round-robin
    return client = elasticsearch.Client({
        requestTimeout: 240000,
        hosts: hosts
    });
};


function addMinutes(date, minutes) {
    return new Date(date.getTime() + minutes * 60000);
}

var bulkIndexPackets = function (packets) {
    var client = getClient();

    var bulkDefaultInstruction = {
        index: {
            _index: 'bmon-detail',
            _type: 'packet'
        }
    };
    var bulkToIndex = {};
    bulkToIndex.body = [];

    packets.forEach(function (packet) {
        bulkToIndex.body.push(bulkDefaultInstruction);
        bulkToIndex.body.push(packet);
    });

    client.bulk(bulkToIndex, function (err, resp) {
        if (err) {
            console.warn("ERROR - BULK INDEX PACKETS");
            console.warn(err);

            bulkIndexPackets(packets);

        } else {
            console.warn(" all is well bip", packets.length);
        }
        client.close();

    });
};

var indexAggregation = function (aggregation) {

    if (aggregation.packets.length > 0) {
        bulkIndexPackets(aggregation.packets);
    }

    var up = aggregation.up;
    var down = aggregation.down;
    console.error({
        type: aggregation.aggregationType.name,
        dateTime: aggregation.dateTime,
        up: up,
        down: down
    });
    var client = getClient();
    var toindex = {
        index: 'bmon',
        type: aggregation.aggregationType.name,
        id: new Date(aggregation.dateTime).toJSON(),
        body: {
            dateTime: aggregation.dateTime,
            up: up,
            down: down
        }
    };
    client.index(toindex,
        function (err, resp) {
            if (err) {
                console.warn("ERROR - INDEX AGG - " + aggregation.aggregationType.name);
                console.warn(err);

                // reindex packets, but first, remove the packets from the aggregation. They have already been passed off to the client
                aggregation.packets = [];
                indexAggregation(aggregation);

            } else {
                console.warn(" all is well ias", {
                    dateTime: aggregation.dateTime,
                    up: up,
                    down: down
                });
            }
            client.close();

            //console.log(resp);
        });
};

var aggregateCurrentHour = function (start, end, callback) {
    var query = {
        index: 'bmon-detail',
        type: 'packet',
        body: {
            aggregations: {
                agg: {
                    filters: {
                        filters: {
                            download: {
                                and: {
                                    filters: [{
                                        range: {
                                            dateTime: {
                                                from: start,
                                                to: end,
                                                include_lower: true,
                                                include_upper: true
                                            }
                                        }
                                    }, {
                                        prefix: {
                                            destAddr: "192.168.1"
                                        }
                                    }]
                                }
                            },
                            upload: {
                                and: {
                                    filters: [{
                                        range: {
                                            dateTime: {
                                                from: start,
                                                to: end,
                                                include_lower: true,
                                                include_upper: true
                                            }
                                        }
                                    }, {
                                        prefix: {
                                            sourceAddr: "192.168.1"
                                        }
                                    }]
                                }
                            }
                        }
                    },
                    aggregations: {
                        sumagg: {
                            sum: {
                                field: "size"
                            }
                        }
                    }
                }
            }


        }
    };
    var client = getClient();
    client.search(query,
        function (err, resp) {
            if (err) {
                console.warn("ERROR - GET HOUR - ");
                console.warn(err.message);


            } else {
                console.warn(" current hour agg sum", {
                    down: resp.aggregations.agg.buckets.download.sumagg.value,
                    up: resp.aggregations.agg.buckets.upload.sumagg.value
                });
                callback({
                    start: start,
                    down: resp.aggregations.agg.buckets.download.sumagg.value,
                    up: resp.aggregations.agg.buckets.upload.sumagg.value
                })
            }
            // client.close();

            //console.log(resp);
        });
}

var deleteNulls = function (aggregationType, callback) {
    var client = getClient();
    client.deleteByQuery({
        index: 'bmon',
        type: aggregationType.name,
        body: {
            "query": {
                "bool": {
                    "filter": {
                        "missing": {
                            "field": "up"
                        }
                    }
                }
            }
        }
    }).then(function (resp) {
        //console.log(resp);
        if (callback) {

            callback(resp);

        }
        client.close();
    });
};

var deleteFuture = function (aggregationType, callback) {
    var client = getClient();
    client.deleteByQuery({
        index: 'bmon',
        type: aggregationType.name,
        body: {
            query: {
                range: {
                    dateTime: {
                        gte: addMinutes(new Date(), 5)
                    }
                }
            }
        }
    }).then(function (resp) {
        //console.log(resp);
        if (callback) {

            callback(resp);

        }
        client.close();
    });
};

var earliestPacket = function (callback) {
    var query = {
        index: 'bmon-detail',
        type: 'packet',
        body: {
            aggs: {
                earliestp: {
                    min: {
                        field: "dateTime"
                    }
                }
            }
        }
    };
    var client = getClient();
    client.search(query,
        function (err, resp) {
            if (err) {
                console.warn("ERROR - EARLIEST PACKET");
                console.warn(err.message);


            } else {
                console.warn("all is well earliest packet", resp);
                callback(new Date(resp.aggregations.earliestp.value));
            }
            client.close();

            //console.log(resp);
        });

};
module.exports.earliestPacket = earliestPacket;
module.exports.aggregateCurrentHour = aggregateCurrentHour;
module.exports.deleteNulls = deleteNulls;
module.exports.deleteFuture = deleteFuture;
module.exports.indexAggregation = indexAggregation;