//for the log page
monui.controller('SummariesCtrl', function ($scope, $parse, $filter, $routeParams, apiFactory, BandwidthChartFactory, BWToDateFactory, DetailReportFactory) {

    var limit = -1;
    var options = BandwidthChartFactory.createOptions('Bandwidth Usage by Minute', '1 Hour');
    $scope.byMinuteChart = BandwidthChartFactory.createChart('ColumnChart', options, BandwidthChartFactory.getDefaultCols(), []);
  

    apiFactory.getMinutes(60)
        .then(function (data) {
            BandwidthChartFactory.appendBandwidthArr(data, $scope.byMinuteChart.data.rows, limit);

        }, function (error) {
            // promise rejected
            console.log('error', error);
        });

    options = BandwidthChartFactory.createOptions('Bandwidth Usage by Hour', '4 Days');
    $scope.byHourChart = BandwidthChartFactory.createChart('ColumnChart', options, BandwidthChartFactory.getDefaultCols(), []);

    apiFactory.getHours(96)
        .then(function (data) {
            BandwidthChartFactory.appendBandwidthArr(data, $scope.byHourChart.data.rows, limit);

        }, function (error) {
            // promise rejected
            console.log('error', error);
        });

    options = BandwidthChartFactory.createOptions('Bandwidth Usage by Day', '~3 Months');
    $scope.byDayChart = BandwidthChartFactory.createChart('ColumnChart', options, BandwidthChartFactory.getDefaultCols(), []);

    apiFactory.getDays(90)
        .then(function (data) {
            BandwidthChartFactory.appendBandwidthArr(data, $scope.byDayChart.data.rows, limit);

        }, function (error) {
            // promise rejected
            console.log('error', error);
        });

    $scope.selected = function (data, selected, title) {
        $scope.loading = true;
        $scope.detailStatus = [];
        $scope.chartableObjects = [];


        $('#detail').modal();

        var dateStr = data.rows[selected.row].c[0].v;

        var startEndDateTimeObject = BWToDateFactory.getStartEndDateTimes(title, dateStr);

        $scope.detailStatus.push("Retrieving data from server...");

        apiFactory.getDetails(startEndDateTimeObject.startDateTime, startEndDateTimeObject.endDateTime).then(function (data) {
                $scope.detailStatus.push("Building Detail Report...");
                $scope.chartableObjects = DetailReportFactory.getNonTotalObjects(data);
                $scope.totalUp = data.totalUp;
                $scope.totalDown = data.totalDown;
                $scope.loading = false;

            },
            function (error) {
                // promise rejected
                console.error('error ', error);
            });
    };

});