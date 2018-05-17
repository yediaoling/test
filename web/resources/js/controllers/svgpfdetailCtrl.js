/**
 * Created by yediaoling on 2016/3/24.
 */

(function () {
    appPW.controller(
        'svgpfdetailCtrl',function($scope, $routeParams,$http,faultlineService) {
            var vm =this;
            var WeakUrl = "WeakLink";
            vm.title = 'svgdetailCtrl.html';
            vm.svgid =  $routeParams.svgid;
            vm.svgsrc = './resources/svgpf/'+vm.svgid + '.svg';
            vm.WeaklinkJS123 = function(moni_num){
                console.log("mo ni bian hao:"+moni_num);
                $http.get(WeakUrl,
                    {
                        params: {
                            dbMode: encodeURI("WeaklinkJS"),
                            moninum:moni_num
                        }
                    }).
                    success(function (data , status, headers, config) {

                        //region 故障恢复相关
                        // -----------清除上次的动画效果----------------------------------------
                        var subStationId = faultlineService.getSubStation();
                        var subStationSec = $(subStationId);
                        subStationSec.children("path").attr("stroke-width","0.4");
                        subStationSec.children("path").attr("class","SubStation");
                        subStationSec.children("use").attr("class","kV10GZ");
                        //-----------------------------------------------------------------
                        var postID = faultlineService.getID();
                        var postSection = $('#'+postID);
                        postSection.children("path").attr("stroke-width","0.4");
                        postSection.children("path").attr("stroke","#00FF00");
                        postSection.children("polyline").attr("stroke","#00FF00");
                        postSection.children("polygon").attr("fill","#00FF00");
                        window.clearInterval(faultlineService.gettimer());

                        var IDs = new Array(data[0].length);
                        var lineSections = new Array(data[0].length);
                        //var IDs = new Array(1);
                        //var lineSections = new Array(1);
                        //data[0]=["monixianlu2"];
                        for(var i = 0; i < data[0].length; i++){
                            IDs[i] = data[0][i];
                            lineSections[i] = $('#'+IDs[i]);
                            lineSections[i].children("path").attr("stroke-width","0.6");
                            lineSections[i].children("path").attr("stroke","#FF0000");
                            lineSections[i].children("polyline").attr("stroke","#FF0000");
                            lineSections[i].children("polygon").attr("fill","#FF0000");
                        }
                        var timer = setInterval(function () {
                            for(var i = 0; i < data[0].length; i++) {
                                lineSections[i].fadeOut(200).fadeIn(200);
                            }
                        },600);
                        faultlineService.settimer(timer);
                        faultlineService.setID(IDs);

                        var table;
                        table = '<table class="table table-hover" style="background-color: #ffffff">' +
                            '<caption>' +
                            '<h1 class="text-center">薄弱环节识别结果</h1>' +
                            '</caption>' +
                            '<thead >' +
                            '<tr >' +
                            '<th class="text-center">薄弱环节</th>' +
                            '<th class="text-center">对象编号</th>' +
                            '</tr>' +
                            '</thead>' +
                            '<tbody>';

                        table = table + '<tr>';
                        table = table + '<td class="text-center">' + "薄弱线路" + '</td>';
                        table = table + '<td class="text-center">' + IDs[0] + '</td>';
                        table = table + '</tr>';

                        if(IDs[1]!=undefined){
                            table = table + '<tr>';
                            table = table + '<td class="text-center">' + 2 + '</td>';
                            table = table + '<td class="text-center">' + IDs[1] + '</td>';
                            table = table + '</tr>';}


                        table = table + '<tr>';
                        table = table + '<td class="text-center">' + "薄弱节点" + '</td>';
                        table = table + '<td class="text-center">' + data[1] + '</td>';
                        table = table + '</tr>';


                        table = table + '</tbody></table>';

                        $('#box1').html(table) ;
                        $.blockUI({ //当点击事件发生时调用弹出层
                            message: $('#box1'), //要弹出的元素box·7

                            css: { //弹出元素的CSS属性
                                top: '15%',
                                left: '50%',
                                textAlign: 'left',
                                marginLeft: '-320px',
                                marginTop: '-145px',
                                width: '800px',
                                background: 'none',
                                fadeOut:  200,
                                fadeIn:  200
                            }
                        });
                        $('.blockOverlay').attr('title', '单击关闭').click($.unblockUI); //遮罩层属性的设置以及当鼠标单击遮罩层可以关闭弹出层
                        $('.close').click($.unblockUI); //也可以自定义一个关闭按钮来关闭弹出层
                    });
            };
            vm.WeaklinkJS = function(){
                $http.get(WeakUrl,
                    {
                        params: {
                            dbMode: encodeURI("WeaklinkJS"),
                            moninum:4
                        }
                    }).
                    success(function (data , status, headers, config) {

                        //region 故障恢复相关
                        // -----------清除上次的动画效果----------------------------------------
                        var subStationId = faultlineService.getSubStation();
                        var subStationSec = $(subStationId);
                        subStationSec.children("path").attr("stroke-width","0.4");
                        subStationSec.children("path").attr("class","SubStation");
                        subStationSec.children("use").attr("class","kV10");
                        //-----------------------------------------------------------------
                        var postID = faultlineService.getID();
                        var postSection = $('#'+postID);
                        postSection.children("path").attr("stroke-width","0.4");
                        window.clearInterval(faultlineService.gettimer());

                        var IDs = new Array(data[0].length);
                        var lineSections = new Array(data[0].length);

                        for(var i = 0; i < data[0].length; i++){
                            IDs[i] = data[0][i];
                            lineSections[i] = $('#'+IDs[i]);
                            lineSections[i].children("path").attr("stroke-width","2");
                            lineSections[i].children("path").attr("class","kV10GZ");
                        }
                        var timer = setInterval(function () {
                            for(var i = 0; i < data[0].length; i++) {
                                lineSections[i].fadeOut(200).fadeIn(200);
                            }
                        },600);
                        faultlineService.settimer(timer);
                        faultlineService.setID(IDs);

                        var table;
                        table = '<table class="table table-hover" style="background-color: #ffffff">' +
                            '<caption>' +
                            '<h1 class="text-center">薄弱环节识别结果</h1>' +
                            '</caption>' +
                            '<thead >' +
                            '<tr >' +
                            '<th class="text-center">序号</th>' +
                            '<th class="text-center">线路编号</th>' +
                            '</tr>' +
                            '</thead>' +
                            '<tbody>';

                        table = table + '<tr>';
                        table = table + '<td class="text-center">' + 1 + '</td>';
                        table = table + '<td class="text-center">' + IDs[0] + '</td>';
                        table = table + '</tr>';

                        table = table + '<tr>';
                        table = table + '<td class="text-center">' + 2 + '</td>';
                        table = table + '<td class="text-center">' + IDs[1] + '</td>';
                        table = table + '</tr>';

                        table = table + '<tr>';
                        table = table + '<td class="text-center">' + 3 + '</td>';
                        table = table + '<td class="text-center">' + IDs[2] + '</td>';
                        table = table + '</tr>';

                        table = table + '<tr>';
                        table = table + '<td class="text-center">' + 4 + '</td>';
                        table = table + '<td class="text-center">' + IDs[3] + '</td>';
                        table = table + '</tr>';

                        table = table + '<tr>';
                        table = table + '<td class="text-center">' + 5 + '</td>';
                        table = table + '<td class="text-center">' + IDs[4] + '</td>';
                        table = table + '</tr>';

                        table = table + '<tr>';
                        table = table + '<td class="text-center">' + 6 + '</td>';
                        table = table + '<td class="text-center">' + IDs[5] + '</td>';
                        table = table + '</tr>';

                        table = table + '<tr>';
                        table = table + '<td class="text-center">' + 7 + '</td>';
                        table = table + '<td class="text-center">' + IDs[6] + '</td>';
                        table = table + '</tr>';

                        table = table + '<tr>';
                        table = table + '<td class="text-center">' + "当前供电能力" + '</td>';
                        table = table + '<td class="text-center">' + 211.2 + '</td>';
                        table = table + '</tr>';

                        table = table + '</tbody></table>';

                        $('#box1').html(table) ;
                        $.blockUI({ //当点击事件发生时调用弹出层
                            message: $('#box1'), //要弹出的元素box
                            css: { //弹出元素的CSS属性
                                top: '15%',
                                left: '50%',
                                textAlign: 'left',
                                marginLeft: '-320px',
                                marginTop: '-145px',
                                width: '800px',
                                background: 'none',
                                fadeOut:  200,
                                fadeIn:  200
                            }
                        });
                        $('.blockOverlay').attr('title', '单击关闭').click($.unblockUI); //遮罩层属性的设置以及当鼠标单击遮罩层可以关闭弹出层
                        $('.close').click($.unblockUI); //也可以自定义一个关闭按钮来关闭弹出层
                    });
            };


            $(document).ready(function () {
                clearInterval(faultlineService.gettimer());
                console.log(faultlineService.gettimer());
                console.log(vm.svgid);
                if(vm.svgid == "moni1")
                    vm.WeaklinkJS123(1);
                if(vm.svgid == "moni2")
                    vm.WeaklinkJS123(2);
                if(vm.svgid == "moni3")
                    vm.WeaklinkJS123(3);
                if(vm.svgid == "moni5")
                    vm.WeaklinkJS123(5);

                if(vm.svgid == "moni6")
                    vm.WeaklinkJS123(6);
                if(vm.svgid == "shiji")
                    vm.WeaklinkJS123(7);

                if(vm.svgid == "shangjiao")
                    vm.WeaklinkJS();
            });

        }
    );
})();