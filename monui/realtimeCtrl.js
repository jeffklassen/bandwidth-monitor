
//for the log page
monui.controller('RealtimeCtrl', function($scope, $filter, $routeParams, $timeout, $filter, apiFactory, BandwidthChartFactory, DetailReportFactory) {
    var tmpData;
    var rows;
    $scope.chartObject = {};
    $scope.detailDates = [];
    var limit = 300;

    var updateChart = function() {
        apiFactory.getSeconds(5)
            .then(function(data) {
                tmpData = data;
                BandwidthChartFactory.appendBandwidthArr(data, $scope.chartObject.data.rows, limit);
                //$scope.chartObject.data.rows.push(rows);

                $timeout(updateChart, 2000);


            }, function(error) {
                console.error('error ', error);
            });
    };

    apiFactory.getSeconds(limit)
        .then(function(data) {
            tmpData = data;
            BandwidthChartFactory.appendBandwidthArr(data, $scope.chartObject.data.rows, limit);
            //$scope.chartObject.data.rows.push(rows);
            //console.log(rows);

            $timeout(updateChart, 5000);


        }, function(error) {
            console.error('error ', error);
        });

    var options = BandwidthChartFactory.createOptions('Bandwidth Usage', '5 Minutes');

    $scope.chartObject = BandwidthChartFactory.createChart('LineChart', options, BandwidthChartFactory.getDefaultCols(), []);

    $scope.selected = function(data, selected, title) {
        var dateStr = data.rows[selected.row].c[0].v;

        $scope.detailDates.splice(0, 0, {
            'date': dateStr
        });
        if ($scope.detailDates.length > 2) {
            $scope.detailDates.pop();
        }

    };

    $scope.getDetails = function() {
        var orderBy = $filter('orderBy');
        var dates = orderBy($scope.detailDates, 'date', false);
        console.log(dates);

        $scope.loading = true;
        $scope.detailStatus = [];
        $scope.chartableObjects = [];


        $('#detail').modal();

        $scope.detailStatus.push("Retrieving data from server...");

        apiFactory.getDetails(dates[0].date, dates[1].date).then(function(data) {
                $scope.detailStatus.push("Building Detail Report...");
                $scope.chartableObjects = DetailReportFactory.getNonTotalObjects(data);
                $scope.totalUp = data.totalUp;
                $scope.totalDown = data.totalDown;
                $scope.loading = false;

            },
            function(error) {
                // promise rejected
                console.error('error ', error);
            });
    };

});