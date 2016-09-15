var BaseChartFactory = (function () {
    return {
        createChart: function (type, options, cols, rows) {
            var chart = {};
            chart.type = type;
            chart.options = options;
            chart.data = {};
            chart.data.cols = cols;
            chart.data.rows = rows;
            chart.formatters = {
                "number": [
                    {
                        "columnNum": 1,
      },
                    {
                        "columnNum": 2,
      }
    ]
            }

            return chart;
        },
        createOptions: function (title, hAxisTitle) {
            var options = {
                'title': title,
                curveType: 'function',
                bars: 'horizontal',
                vAxis: {
                    title: "KB",
                    viewWindowMode: 'explicit',
                    viewWindow: {
                        min: 0
                    }
                },
                hAxis: {
                    title: hAxisTitle,
                    textPosition: 'none'
                },
                animation: {
                    duration: 1000,
                    easing: 'in'
                },
                legend: 'none',
                'chartArea': {
                    'width': '90%',
                    'height': '80%'
                },
            }

            return options;
        }
    }
}());
