/**
 * Created by yediaoling on 2016/5/4.
 */

(function () {
    appPW.directive('svgXr', ['$compile','$location','faultlineService',function ($compile,$location,faultlineService) {

        return {
            restrict: 'A'
            ,link: function( scope, element, attrs ) {
                $(document).ready(function () {
                    $('div #mainbody').css("background-color", 'black');

                    var faultID = faultlineService.getID();
                    var lineSection = $('#'+faultID);
                    lineSection.children("path").attr("stroke-width","0.6");
                    lineSection.children("path").attr("stroke","#FF0000");
                    lineSection.children("polyline").attr("stroke","#FF0000");
                    lineSection.children("polygon").attr("fill","#FF0000");
                    window.clearInterval(faultlineService.gettimer());
                    var timer = setInterval(function () {
                        lineSection.fadeOut(200).fadeIn(200);
                    },600);
                    faultlineService.settimer(timer);

                    var svgEle = $(element).find('svg')[0];
                    if (svgEle.getAttribute("id") == '图层_1'){
                        svgEle.setAttribute('height', '1727');
                        svgEle.setAttribute('width', '2112');
                        svgEle.setAttribute('viewBox', '25 365 327.9 220');
                    }
                    if (svgEle.getAttribute("id") == '图层_2'){
                        svgEle.setAttribute('height', '1727');
                        svgEle.setAttribute('width', '2142');
                        svgEle.setAttribute('viewBox', '32 349 361.8 242');
                    }
                    if (svgEle.getAttribute("id") == '图层_3'){
                        svgEle.setAttribute('height', '1727');
                        svgEle.setAttribute('width', '2142');
                        svgEle.setAttribute('viewBox', '27 369 298.9 199.6');
                    }
                    if (svgEle.getAttribute("id") == '图层_5'){
                        svgEle.setAttribute('height', '1209');
                        svgEle.setAttribute('width', '2394');
                        svgEle.setAttribute('viewBox', '137 332 133.2 172.5');
                    }
                    if (svgEle.getAttribute("id") == '图层_6'){
                        svgEle.setAttribute('height', '1247');
                        svgEle.setAttribute('width', '2128');
                        svgEle.setAttribute('viewBox', '73 347 217.2 181.5');
                    }
                    if (svgEle.getAttribute("id") == '图层_8'){
                        svgEle.setAttribute('height', '1727');
                        svgEle.setAttribute('width', '2126');
                        svgEle.setAttribute('viewBox', '35 369 298.32 199.65');
                    }
//                    var textLayer = $('#'+"Text_Layer");
//                    textLayer.toggle();

                });
            }
        }
    }]);
})();