BandwidthChartFactory = Object.create(BaseChartFactory);
BandwidthChartFactory.convertElement = function (element) {
    var row = {
        c: [
            {
                v: element.timestamp
                },
            {
                v: element.down
                },
            {
                v: element.up
                }
]
    };
    return row;
};
BandwidthChartFactory.getDefaultCols = function () {
    var cols = [
        {
            id: "t",
            label: "Date",
            type: "string"
                    },
        {
            id: "s",
            label: "KB Down",
            type: "number"
                    },
        {
            id: "s",
            label: "KB Up",
            type: "number"
                    }
    ];

    return cols;
}
BandwidthChartFactory.appendBandwidthArr = function (data, rows, limit) {
    // remove rows already in the chart
    data.bandwidthList = data.bandwidthList.filter(function (bandwidth) {

        var found = false;
        angular.forEach(rows, function (row) {
            if (!found) {
                var timestamp = row.c[0].v
                if (timestamp == bandwidth.timestamp) {
                    found = true;
                }
            }

        });
        return !found;

    });
    // sort data
    data.bandwidthList = data.bandwidthList.sort(function (a, b) {
        if (a.timestamp > b.timestamp) {
            return 1;
        }
        if (a.timestamp < b.timestamp) {
            return -1;
        }
        return 0;
    });
    angular.forEach(data.bandwidthList, function (element) {

        rows.splice(0, 0, this.convertElement(element));
        if (limit > 0) {
            if (rows.length > 300) {
                rows.pop();
            }
        }
    }, this);

    return rows;
};

monui.factory('BandwidthChartFactory', function () {
    return BandwidthChartFactory;
});