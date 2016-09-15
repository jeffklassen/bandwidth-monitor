monui.config(function ($routeProvider) {
    $routeProvider
        .when('/realtime', {
            controller: 'RealtimeCtrl',
            templateUrl: 'realtime.html'
        })
        .when('/summaries', {
            controller: 'SummariesCtrl',
            templateUrl: 'summaries.html'
        })
        .otherwise({
            redirectTo: '/'
        });
});