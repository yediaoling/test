/**
 * Created by yediaoling on 2017/6/20.
 */
(function () {
    appPW.directive('svgXr2', ['$compile','$location','faultlineService',function ($compile,$location,faultlineService) {

        return {
            restrict: 'A'
            ,link: function( scope, element, attrs ) {
                $(document).ready(function () {
                    $('div #mainbody').css("background-color", 'black');

                    var svgEle = $(element).find('svg')[0];
                    if (svgEle.getAttribute("id") == '图层_1'){
                        svgEle.setAttribute('height', '1159');
                        svgEle.setAttribute('width', '2127');
                        svgEle.setAttribute('viewBox', '50 345 271 182');
                    }
                    if (svgEle.getAttribute("id") == '图层_2'){
                        svgEle.setAttribute('height', '1157');
                        svgEle.setAttribute('width', '2127');
                        svgEle.setAttribute('viewBox', '62 322 299 200');
                    }
                    if (svgEle.getAttribute("id") == '图层_3'){
                        svgEle.setAttribute('height', '1727');
                        svgEle.setAttribute('width', '2142');
                        svgEle.setAttribute('viewBox', '21 373 298.87 199.65');
                    }
                    if (svgEle.getAttribute("id") == '图层_5'){
                        svgEle.setAttribute('height', '1322');
                        svgEle.setAttribute('width', '2410');
                        svgEle.setAttribute('viewBox', '122 328 133.2 172.5');
                    }

                });
            }
        }
    }]);
})();