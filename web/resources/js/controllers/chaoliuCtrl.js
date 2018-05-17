/**
 * Created by yediaoling on 2017/6/15.
 */

(function () {
    appPW.controller(
        'chaoliuCtrl',function($scope, $routeParams,$http,faultlineService) {
            var vm =this;
            vm.title = 'chaoliu.html';
            vm.svgid =  $routeParams.svgid;
            vm.svgsrc = './resources/svgpf/'+vm.svgid + '.svg';

            vm.xunhuan = function(){
                var xianzhi = [148,210,279,166,238,308,95,120,420,420,455];
                var jianshu = [32,42,50,24,52,49,36,59,34,34,-44];
                var ID = ["monixianlu1","monixianlu2","monixianlu3","monixianlu4","monixianlu5","monixianlu6","monixianlu7","monixianlu8","monixianlu9","monixianlu10","monixianlu11"];
                for (var i=0;i<8;i++){
                    vm.xunhuan2(ID[i],xianzhi[i],jianshu[i]);
                }
                for (var j=8;j<10;j++){
                    vm.xunhuan3(ID[j],xianzhi[j],jianshu[j]);
                }
                vm.xunhuan4(ID[10],xianzhi[10],jianshu[10]);
            };

            vm.xunhuan2 = function(ID,xianzhi,jianshu){
                var lineSection = $('#'+ID);
                var xyz;
                lineSection.children("polygon").each(function () {
                    xyz = $(this).attr("points");
                    var shuzi;
                    var shuzis;
                    var xyznew;
                    var dijin;
                    shuzi = parseInt(xyz.substring(16,19))+2;
                    if(shuzi > xianzhi){
                        dijin = -jianshu;
                    }else{
                        dijin = 2;
                    }
                    shuzi = parseInt(xyz.substring(0,3))+dijin;
                    shuzis  = shuzi.toString();
                    if(shuzi < 100)
                        shuzis = "0"+shuzis;
                    xyznew = xyz.replace(xyz.substring(0,3),shuzis);
                    xyznew = xyznew.replace(xyz.substring(0,3),shuzis);
                    shuzi = parseInt(xyz.substring(16,19))+dijin;
                    shuzis  = shuzi.toString();
                    if(shuzi < 100)
                        shuzis = "0"+shuzis;
                    xyznew = xyznew.replace(xyz.substring(16,19),shuzis);
                    $(this).attr("points",xyznew);
                });
            };
            vm.xunhuan3 = function(ID,xianzhi,jianshu){
                var lineSection = $('#'+ID);
                var xyz,shuzi,shuzis,xyznew,dijin;
                lineSection.children("polygon").each(function () {
                    xyz = $(this).attr("points");
                    shuzi = parseInt(xyz.substring(24,27))+2;
                    if(shuzi > xianzhi){
                        dijin = -jianshu;
                    }else{
                        dijin = 2;
                    }
                    shuzi = parseInt(xyz.substring(8,11))+dijin;
                    shuzis  = shuzi.toString();
                    xyznew = xyz.replace(xyz.substring(8,11),shuzis);
                    xyznew = xyznew.replace(xyz.substring(8,11),shuzis);
                    shuzi = parseInt(xyz.substring(24,27))+dijin;
                    shuzis  = shuzi.toString();
                    xyznew = xyznew.replace(xyz.substring(24,27),shuzis);
                    $(this).attr("points",xyznew);
                });
            };
            vm.xunhuan4 = function(ID,xianzhi,jianshu){
                var lineSection = $('#'+ID);
                var xyz,shuzi,shuzis,xyznew,dijin;
                lineSection.children("polygon").each(function () {
                    xyz = $(this).attr("points");
                    shuzi = parseInt(xyz.substring(24,27))-2;
                    if(shuzi < xianzhi){
                        dijin = -jianshu;
                    }else{
                        dijin = -2;
                    }
                    shuzi = parseInt(xyz.substring(8,11))+dijin;
                    shuzis  = shuzi.toString();
                    xyznew = xyz.replace(xyz.substring(8,11),shuzis);
                    xyznew = xyznew.replace(xyz.substring(8,11),shuzis);
                    shuzi = parseInt(xyz.substring(24,27))+dijin;
                    shuzis  = shuzi.toString();
                    xyznew = xyznew.replace(xyz.substring(24,27),shuzis);
                    $(this).attr("points",xyznew);
                });
            };
            $(document).ready(function () {
                var timer = setInterval(function(){vm.xunhuan()},300);
                faultlineService.settimer(timer);
            });
        }
    );
})();