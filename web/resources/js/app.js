/**
 * Created by yediaoling on 2016/3/23.
 */

var appPW = angular.module('appPW',[
    'ngRoute'
   ,'ngTable'
]);

appPW.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider
            .when('/view/main',{
                templateUrl: './partials/main.html',
                controller: 'mainCtrl as main'
            })
            .when('/view/svgpfdetail/:svgid',{
                templateUrl: './partials/svgpfdetail.html',
                controller: 'svgpfdetailCtrl as svgpfDetail'
            })
            .when('/view/lianluo',{
                templateUrl: './partials/lianluo.html',
                controller: 'lianluoCtrl as lianluo'
            })
            .when('/view/kaiguan',{
                templateUrl: './partials/kaiguan.html',
                controller: 'kaiguanCtrl as kaiguan'
            })
            .when('/view/chaoliu/:svgid',{
                templateUrl: './partials/chaoliu.html',
                controller: 'chaoliuCtrl as chaoliu'
            })
            .when('/view/yunxing',{
                templateUrl: './partials/yunxing.html',
                controller: 'yunxingCtrl as yunxing'
            })
            .when('/view/fenduan',{
                templateUrl: './partials/fenduan.html',
                controller: 'fenduanCtrl as fenduan'
            })
            .when('/view/zhongyaodu',{
                templateUrl: './partials/zhongyaodu.html',
                controller: 'zhongyaoduCtrl as zyd'
            })
            .otherwise({
                redirectTo: '/view/main'
            })
    }]);