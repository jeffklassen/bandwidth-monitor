monui.factory('DetailReportFactory', function (DetailChartFactory) {

    isEmpty = function (obj) {

        // null and undefined are "empty"
        if (obj == null) return true;

        // Assume if it has a length property with a non-zero value
        // that that property is correct.
        if (obj.length > 0) return false;
        if (obj.length === 0) return true;

        // Otherwise, does it have any properties of its own?
        // Note that this doesn't handle
        // toString and valueOf enumeration bugs in IE < 9
        for (var key in obj) {
            if (hasOwnProperty.call(obj, key)) return false;
        }

        return true;
    };
    // sortObjects = 
    return {
        
        getNonTotalObjects: function (data) {
            var objects = [];
            
            if (!isEmpty(data.downloadByExternalService)) {

                objects.push({
                    name: "downloadByExternalService",
                    displayName: "Download By External Service",
                    object: data.downloadByExternalService
                });
            }
            if (!isEmpty(data.downloadByInternalService)) {
                objects.push({
                    name: "downloadByInternalService",
                    displayName: "Download By Internal Service",
                    object: data.downloadByInternalService
                });
            }
            if (!isEmpty(data.downloadByExternalUrl)) {
                objects.push({
                    name: "downloadByExternalUrl",
                    displayName: "Download By External Url",
                    object: data.downloadByExternalUrl
                });
            }
            if (!isEmpty(data.downloadByInternalUrl)) {
                objects.push({
                    name: "downloadByInternalUrl",
                    displayName: "Download By Internal Url",
                    object: data.downloadByInternalUrl
                });
            }
            if (!isEmpty(data.uploadByExternalService)) {
                objects.push({
                    name: "uploadByExternalService",
                    displayName: "Upload By External Service",
                    object: data.uploadByExternalService
                });
            }
            if (!isEmpty(data.uploadByInternalService)) {
                objects.push({
                    name: "uploadByInternalService",
                    displayName: "Upload By Internal Service",
                    object: data.uploadByInternalService
                });
            }
            if (!isEmpty(data.uploadByExternalUrl)) {
                objects.push({
                    name: "uploadByExternalUrl",
                    displayName: "Upload By External Url",
                    object: data.uploadByExternalUrl
                });
            }
            if (!isEmpty(data.uploadByInternalUrl)) {
                objects.push({
                    name: "uploadByInternalUrl",
                    displayName: "Upload By Internal Url",
                    object: data.uploadByInternalUrl
                });
            }

            angular.forEach(objects, function (chartableObject) {
                options = DetailChartFactory.createOptions('', '');
                var rows = DetailChartFactory.getRows(chartableObject.object);
                chartableObject.chart = DetailChartFactory.createChart('PieChart', options, DetailChartFactory.getDefaultCols(), rows);

            });

            return objects;
        }
    };
});