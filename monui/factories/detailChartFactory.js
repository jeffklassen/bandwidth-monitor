DetailChartFactory = Object.create(BaseChartFactory);
DetailChartFactory.getDefaultCols = function () {
    var cols = [
        {
            id: "t",
            label: "Key",
            type: "string"
                    },
        {
            id: "s",
            label: "Size",
            type: "number"
                    }
    ];
    return cols;
};

DetailChartFactory.convertElement = function (element) {
    var row = {
        c: [
            {
                v: element.key
                            },
            {
                v: element.value
                            }
        ]
    };
    return row;
};
DetailChartFactory.getRows = function (entries) {
    var rows = [];
    
    angular.forEach(entries, function (entry) {
        rows.push(this.convertElement(entry));
    }, this);
    
    return rows;
};

monui.factory('DetailChartFactory', function () {
    return DetailChartFactory;
});