/**
 * Created by liyanjun on 2017/6/17.
 */

(function () {
    appPW.controller(
        'lianluoCtrl',function($scope,$http,ngTableParams) {
            var vm= this;
            vm.title = 'lianluo.html';
            var url = "ResilienceModel";
            readDB(url);

            vm.refresh = function () {
                console.log("刷新");
                readDB(url);
            };

            function readDB(url) {
                $http.get(url,
                    {
                        params: {
                            dbMode: encodeURI("kaiguanshuju")
                        }
                    }).
                    success(function (data, status, headers, config) {
                        console.log(data);
                        console.log(data.length);
                        var table  = new ngTableParams({
                            page: 1,            // show first page
                            count: 15           // count per page
                        }, {
                            total: data.length, // length of data
                            getData: function ($defer, params) {
                                $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                            }
                        });
                        vm.risk_access = table;
                    });
            }

        }
    );
})();