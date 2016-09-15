monui.factory('BWToDateFactory', function ($http, $q) {
    var pad = function (num) {
        var size = 2;
        var s = num + "";
        while (s.length < size) s = "0" + s;
        return s;
    };

    var ChartTypeEnum = {
        MINUTE: 0,
        HOUR: 1,
        DAY: 2
    };
    
    var mapTitleToChart = function (title) {
        if (title.indexOf('Minute') > -1) {
            return ChartTypeEnum.MINUTE;
        } else if (title.indexOf('Hour') > -1) {
            return ChartTypeEnum.HOUR;
        } else if (title.indexOf('Day') > -1) {
            return ChartTypeEnum.DAY;
        }
    };
    
    var getDateFromString = function (dateStr) {
        var reggie = /(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})/;
        var dateArray = reggie.exec(dateStr);
        var dateObject = new Date(
            (+dateArray[1]), (+dateArray[2]) - 1, // Careful, month starts at 0!
            (+dateArray[3]), (+dateArray[4]), (+dateArray[5]), (+dateArray[6])
        );
        return dateObject;
    };
    
    var getStringFromDate = function (date) {
        return date.getFullYear() + '-' + pad(date.getMonth() + 1) + '-' + pad(date.getDate()) + ' ' + pad(date.getHours()) + ':' + pad(date.getMinutes()) + ':' + pad(date.getSeconds());
    };
    
    var getEndDate = function (startDate, chartType) {
        switch (chartType) {
        case ChartTypeEnum.MINUTE:
            return new Date(startDate.getTime() + (1 * 60000) - 1000);
        case ChartTypeEnum.HOUR:
            return new Date(startDate.getTime() + (60 * 60000) - 1000);
        case ChartTypeEnum.DAY:
            return new Date(startDate.getTime() + (24 * 60 * 60000) - 1000);
        }
    };

    return {

        getStartEndDateTimes: function (chartName, startDateStr) {
            var chartType = mapTitleToChart(chartName);
            var startDate = getDateFromString(startDateStr);
            var endDate = getEndDate(startDate, chartType);
            var endDateStr = getStringFromDate(endDate);

            return {
                startDateTime: startDateStr,
                endDateTime: endDateStr
            };
        }

    };
});