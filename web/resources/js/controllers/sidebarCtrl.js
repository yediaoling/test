/**
 * Created by yediaoling on 2016/4/15.
 */

(function () {
    appPW.controller(
        'sidebarCtrl',function($scope,$routeParams,$http,faultlineService) {
            var vm = this;
            var url = "FaultSectionLocation";
            var url2 = "shishiJiance";
            var region = "";
            var ID = "123";
            var faulttype = "";
            var faultphase = "";
            var mainUsageNames = [
                {'name': '首页', 'id': 'main', 'class': 'glyphicon glyphicon-home', 'status': 'active'}
            ];
            var riskShow = [
                {'name': '模拟拓扑1', 'id': 'svgpfdetail/moni1', 'class': 'glyphicon glyphicon-tower','status': ''}
                ,{'name': '模拟拓扑2', 'id': 'svgpfdetail/moni2', 'class': 'glyphicon glyphicon-tower','status': ''}
                ,{'name': '模拟拓扑3', 'id': 'svgpfdetail/moni3', 'class': 'glyphicon glyphicon-tower','status': ''}
//                ,{'name': '模拟拓扑4', 'id': 'svgpfdetail/moni5', 'class': 'glyphicon glyphicon-tower','status': ''}
//                ,{'name': '模拟拓扑5', 'id': 'svgpfdetail/moni6', 'class': 'glyphicon glyphicon-tower','status': ''}
                ,{'name': '实际拓扑', 'id': 'svgpfdetail/shiji', 'class': 'glyphicon glyphicon-tower','status': ''}
            ];
            var resilienceShow = [
                {'name': '10kV出线实时运行数据', 'id': 'yunxing', 'class': 'glyphicon glyphicon-file','status': ''}
                ,{'name': '10kV开关重要度分析', 'id': 'lianluo', 'class': 'glyphicon glyphicon-file','status': ''}
                ,{'name': '实际拓扑开关重要度分析', 'id': 'kaiguan', 'class': 'glyphicon glyphicon-file','status': ''}
                ,{'name': '10kV出线潮流流向分析', 'id': 'chaoliu/moni2', 'class': 'glyphicon glyphicon-file','status': ''}
            ];
            var configUsageNames = [
//                {'name': '系统设置', 'id': 'configSys', 'class': 'glyphicon glyphicon-cog','status': ''},
                {'name': '用户管理', 'id': 'configUser', 'class': 'glyphicon glyphicon-cog','status': ''}
//                {'name': '日志查询', 'id': 'configLog', 'class': 'glyphicon glyphicon-cog','status': ''}
            ];
            var zongheCtrls = [
                {'name': '实时负荷预测', 'id': 'zhongyaodu', 'class': 'glyphicon glyphicon-cog','status': ''},
                {'name': '出力优化调度', 'id': 'fenduan', 'class': 'glyphicon glyphicon-cog','status': ''}
            ];
            var faultIndicator = [
                {'name':'模拟线路1过流','id':'11758949','status':''},
                {'name':'模拟线路2过流','id':'11752944','status':''},
                {'name':'模拟线路3过流','id':'11755904','status':''},
                {'name':'模拟线路4过流','id':'17234212','status':''},
                {'name':'模拟线路5过流','id':'17234379','status':''},
                {'name':'模拟线路6过流','id':'19950952','status':''},
                {'name':'模拟线路7过流','id':'23919192','status':''},
                {'name':'模拟线路8过流','id':'23918564','status':''}
//                {'name':'配电自动化终端9','id':'23919150','status':''},
            ];

            vm.mainUsageNames = mainUsageNames;
            vm.riskShow = riskShow;
            vm.resilienceShow = resilienceShow;
            vm.configUsageNames = configUsageNames;
            vm.faultIndicator = faultIndicator;
            vm.zongheCtrls = zongheCtrls;
            vm.checkID = false;

            vm.faultLocation = function(){
                var FIs = document.getElementsByName("FI");
                var FI_ID = [];
                var num = 0;
                vm.svgid =  $routeParams.svgid;
                for (var i = 0; i < FIs.length; i++) {
                    $('#' + FIs[i].value).children("path").attr("stroke","#00FF00");
                    if (FIs[i].checked == true) {
                        FI_ID[num] = FIs[i].value;
                        $('#' + FI_ID[num]).children("path").attr("stroke","#FF0000");
                        num++;
                    }
                }

                $http.get(url,
                    {
                        params: {
                            dbMode: encodeURI("faultLocation"),
                            num: num,
                            fiid: FI_ID,
                            svg:vm.svgid,
                            moninum: 99
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
                        //endregion

                        var postID = faultlineService.getID();
                        var postSection = $('#'+postID);
                        postSection.children("path").attr("stroke-width","0.4");
                        postSection.children("path").attr("stroke","#00FF00");
                        postSection.children("polyline").attr("stroke","#00FF00");
                        postSection.children("polygon").attr("fill","#00FF00");
                        var postID2 = "monixianlu1";
                        postSection = $('#'+postID2);
                        postSection.children("path").attr("stroke-width","0.4");
                        postSection.children("path").attr("stroke","#00FF00");
                        postSection.children("polyline").attr("stroke","#00FF00");
                        postSection.children("polygon").attr("fill","#00FF00");
                        window.clearInterval(faultlineService.gettimer());

                        ID = data[0].ID;
                        var lineSection = $('#'+ID);
                        console.log(lineSection);
                        lineSection.children("path").attr("stroke-width","0.6");
                        lineSection.children("path").attr("stroke","#FF0000");
                        lineSection.children("polyline").attr("stroke","#FF0000");
                        lineSection.children("polygon").attr("fill","#FF0000");
                        var timer = setInterval(function () {
                            lineSection.fadeOut(200).fadeIn(200);
                        },600);
                        faultlineService.settimer(timer);
                        faultlineService.setID(ID);
                        $scope.ID = ID;

                        if(ID == "遥信信息错误"){
                            faulttype = "NULL";
                            faultphase = "NULL";
                        }else{
                            faulttype = "三相接地故障";
                            faultphase = "ABC相";
                        }
                        $scope.faulttype = faulttype;
                        $scope.faultphase = faultphase;


                        var table;
                        table = '<table class="table table-hover" style="background-color: #ffffff">' +
                            '<caption>' +
                            '<h1 class="text-center">故障诊断结果</h1>' +
                            '</caption>' +
                            '<thead >' +
                            '<tr >' +
                            '<th class="text-center">故障线路编号</th>' +
                            '<th class="text-center">故障类型</th>' +
                            '<th class="text-center">故障相</th>' +
                            '</tr>' +
                            '</thead>' +
                            '<tbody>';

                            table = table + '<tr>';
                            table = table + '<td class="text-center">' + ID + '</td>';
                            table = table + '<td class="text-center">' + faulttype + '</td>';
                            table = table + '<td class="text-center">' + faultphase + '</td>';
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

            vm.textHide = function(){
                var textLayer = $('#'+"Text_Layer");
                textLayer.toggle();
            };

            vm.faultRestore = function(){
                var FIs = document.getElementsByName("FI");
                var FI_ID = [];
                var num = 0;
                for (var i = 0; i < FIs.length; i++) {
                    $('#' + FIs[i].value).children("path").attr("stroke","#00FF00");
                    if (FIs[i].checked == true) {
                        FI_ID[num] = FIs[i].value;
                        $('#' + FI_ID[num]).children("path").attr("stroke","#FF0000");
                        num++;
                    }
                }
                $http.get(url,
                    {
                        params: {
                            dbMode: encodeURI("faultLocation"),
                            num: num,
                            fiid: FI_ID,
                            svg:vm.svgid,
                            moninum:  $routeParams.svgid
                        }
                    }).
                    success(function (data , status, headers, config) {
                        var temp = 0;
                        var faultID = data[0].ID;
                        var schemeNumber = data[0]["schemeNumber"];
                        if(schemeNumber==0|vm.svgid=="moni3"){
                            layer.alert("恢复方案不存在");
                        }
                        else {
                            var restorablePower = data[0].restorablePower;
                            var tie2close = data[0]["10"];
                            var nodeNumber = data[0].nodeNumber;

                            var table;
                            table = '<table id="rounded-corner" >' +
                                '<caption>' +
                                '<h1 class="text-center">故障恢复方案</h1>' +
                                '</caption>' +
                                '<thead >' +
                                '<tr >' +
                                '<th class="text-center" >恢复方案</th>' +
                                '<th class="text-center" >故障原因</th>' +
                                '<th class="text-center">失电负荷</th>' +
                                '<th class="text-center">恢复需要操作开关</th>' +
                                '<th class="text-center">可恢复量</th>' +
                                '<th class="text-center">平均电压</th>' +
                                '<th class="text-center" width="30px">节点电压</th>' +
                                '</tr>' +
                                '</thead>' +
                                '<tbody>';
                            for (var i = 0; i < schemeNumber; i++) {
                                table = table + '<tr>';
                                table = table + '<td class="text-center">' + '#' + (i + 1) + '</td>'
                                table = table + '<td class="text-center">' + faultID + '号线路故障' + '</td>';
                                table = table + '<td class="text-center">' + restorablePower + 'MW' + '</td>';
                                table = table + '<td class="text-center">' + data[0][(10 + i) + ""] + '</td>';
                                table = table + '<td class="text-center">' + restorablePower + 'MW' + '</td>';
                                table = table + '<td class="text-center">' + data[0][(30 + i) + ""] + 'kV' + '</td>';
                                table = table + '<td class="text-center" width="30px">' + '<button>详情</button>' + '</td>';
                                table = table + '</tr>';
                            }
                            table = table + '</tbody></table>';
                            $('#box3').html(table);
                            for (var i = 0; i < schemeNumber; i++) {
                                $("#box3").find("button").eq(i).attr("id",i);
                            }

                            layer.open({
                                type: 1 //Page层类型
                                , area: ['900px', '400px'], title: '', shade: 0.6 //遮罩透明度
                                , maxmin: true //允许全屏最小化
                                , anim: 0 //0-6的动画形式，-1不开启
                                , content: $('#box3'), moveOut: true
                            });

                            var chartDatas = new Array(schemeNumber);
                            for(var i=0;i<schemeNumber;i++) {
                                chartDatas[i] =new Array(nodeNumber);
                                for (var j = 0; j < nodeNumber; j++) {
                                    chartDatas[i][j] = new Object();
                                    chartDatas[i][j].bus = j + 1;
                                    chartDatas[i][j].voltage = data[0][i * 10 +(20 + j) + ""]
                                }
                            }

                                $("button#0").click(function () {
                                    //按钮【按钮一】的回调
                                    AmCharts.makeChart("chartdiv", {
                                        "type": "serial",
                                        "dataProvider": chartDatas[0],
                                        "categoryField": "bus",
                                        "graphs": [
                                            {
                                                "valueField": "voltage",
                                                "type": "line",
                                                "fillAlphas": 0,
                                                //"angle": 30,
                                                //"depth3D": 15,
                                                "bullet": "round",
                                                "lineColor": "#8d1cc6",
                                                "balloonText": "[[category]]:<b>[[value]]</b>"
                                            }
                                        ],
                                        "allLabels": [
                                            {
                                                "text": "节点电压曲线",
                                                "align": "center",
                                                "bold": "true",
                                                "size": 20,
                                                "y": 10
                                            }
                                        ],
                                        "marginTop": 50
                                    });

                                    layer.open({
                                        type: 1 //Page层类型
                                        , area: ['1000px', '600px'], title: '', shade: 0.6 //遮罩透明度
                                        , maxmin: true //允许全屏最小化
                                        , anim: 0 //0-6的动画形式，-1不开启
                                        , content: $('#chartdiv'), moveOut: true
                                    });

                                });
                                $("button#1").click(function () {
                                //按钮【按钮一】的回调
                                AmCharts.makeChart("chartdiv", {
                                    "type": "serial",
                                    "dataProvider": chartDatas[1],
                                    "categoryField": "bus",
                                    "graphs": [
                                        {
                                            "valueField": "voltage",
                                            "type": "line",
                                            "fillAlphas": 0,
                                            //"angle": 30,
                                            //"depth3D": 15,
                                            "bullet": "round",
                                            "lineColor": "#8d1cc6",
                                            "balloonText": "[[category]]:<b>[[value]]</b>"
                                        }
                                    ],
                                    "allLabels": [
                                        {
                                            "text": "节点电压曲线",
                                            "align": "center",
                                            "bold": "true",
                                            "size": 20,
                                            "y": 10
                                        }
                                    ],
                                    "marginTop": 50
                                });

                                layer.open({
                                    type: 1 //Page层类型
                                    , area: ['1000px', '600px'], title: '', shade: 0.6 //遮罩透明度
                                    , maxmin: true //允许全屏最小化
                                    , anim: 0 //0-6的动画形式，-1不开启
                                    , content: $('#chartdiv'), moveOut: true
                                });

                            });


                        }
                    });

            };

            vm.shishiJiance2 = function(){
                $http.get(url2,
                    {
                        params: {
                            dbMode: encodeURI("shishiJiance")
                        }
                    }).
                    success(function (data , status, headers, config) {
                        ID = data[0].ID;
                        var fType  = data[0].fType;
                        var flag  = faultlineService.getflag();
                        var fPhase  = data[0].fPhase;
                        console.log(ID);
                        console.log(fType);
                        console.log(fPhase);
                        if((fType != "NULL") && (flag != 1)){
                            console.log("捕捉到故障时刻！");

                            flag = 1;
                            faultlineService.setflag(flag);


                            var lineSection = $('#'+ID);
                            console.log(lineSection);
                            lineSection.children("path").attr("stroke-width","0.6");
                            lineSection.children("path").attr("stroke","#FF0000");
                            lineSection.children("polyline").attr("stroke","#FF0000");
                            lineSection.children("polygon").attr("fill","#FF0000");
                            var timer = setInterval(function () {
                                lineSection.fadeOut(200).fadeIn(200);
                            },600);
                            faultlineService.settimer(timer);
                            faultlineService.setID(ID);
                            $scope.ID = ID;

                            var table;
                            table = '<table class="table table-hover" style="background-color: #ffffff">' +
                                '<caption>' +
                                '<h1 class="text-center">故障诊断结果</h1>' +
                                '</caption>' +
                                '<thead >' +
                                '<tr >' +
                                '<th class="text-center">故障线路编号</th>' +
                                '<th class="text-center">故障类型</th>' +
                                '<th class="text-center">故障相</th>' +
                                '</tr>' +
                                '</thead>' +
                                '<tbody>';

                            table = table + '<tr>';
                            table = table + '<td class="text-center">' + ID + '</td>';
                            table = table + '<td class="text-center">' + fType + '</td>';
                            table = table + '<td class="text-center">' + fPhase + '</td>';
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

                            var faultID = data[0].ID;
                            var schemeNumber = data[0]["schemeNumber"];
                            if(schemeNumber==0|vm.svgid=="moni3"){
                                layer.alert("恢复方案不存在");
                            }
                            else {
                                var restorablePower = data[0].restorablePower;
                                var tie2close = data[0]["10"];
                                var nodeNumber = data[0].nodeNumber;

                                var table2;
                                table2 = '<table id="rounded-corner" >' +
                                    '<caption>' +
                                    '<h1 class="text-center">故障恢复方案</h1>' +
                                    '</caption>' +
                                    '<thead >' +
                                    '<tr >' +
                                    '<th class="text-center" >恢复方案</th>' +
                                    '<th class="text-center" >故障原因</th>' +
                                    '<th class="text-center">失电负荷</th>' +
                                    '<th class="text-center">恢复需要操作开关</th>' +
                                    '<th class="text-center">可恢复量</th>' +
                                    '<th class="text-center">平均电压</th>' +
                                    '<th class="text-center" width="30px">节点电压</th>' +
                                    '</tr>' +
                                    '</thead>' +
                                    '<tbody>';
                                for (var i = 0; i < schemeNumber; i++) {
                                    table2 = table2 + '<tr>';
                                    table2 = table2 + '<td class="text-center">' + '#' + (i + 1) + '</td>'
                                    table2 = table2 + '<td class="text-center">' + faultID + '号线路故障' + '</td>';
                                    table2 = table2 + '<td class="text-center">' + restorablePower + 'MW' + '</td>';
                                    table2 = table2 + '<td class="text-center">' + data[0][(10 + i) + ""] + '</td>';
                                    table2 = table2 + '<td class="text-center">' + restorablePower + 'MW' + '</td>';
                                    table2 = table2 + '<td class="text-center">' + data[0][(30 + i) + ""] + 'kV' + '</td>';
                                    table2 = table2 + '<td class="text-center" width="30px">' + '<button>详情</button>' + '</td>';
                                    table2 = table2 + '</tr>';
                                }
                                table2 = table2 + '</tbody></table>';
                                $('#box3').html(table2);
                                for (var i = 0; i < schemeNumber; i++) {
                                    $("#box3").find("button").eq(i).attr("id",i);
                                }

                                layer.open({
                                    type: 1 //Page层类型
                                    , area: ['900px', '400px'], title: '', shade: 0 //遮罩透明度
                                    , maxmin: true //允许全屏最小化
                                    , anim: 0 //0-6的动画形式，-1不开启
                                    , content: $('#box3'), moveOut: true
                                });

                                var chartDatas = new Array(schemeNumber);
                                for(var i=0;i<schemeNumber;i++) {
                                    chartDatas[i] =new Array(nodeNumber);
                                    for (var j = 0; j < nodeNumber; j++) {
                                        chartDatas[i][j] = new Object();
                                        chartDatas[i][j].bus = j + 1;
                                        chartDatas[i][j].voltage = data[0][i * 10 +(20 + j) + ""]
                                    }
                                }

                                $("button#0").click(function () {
                                    //按钮【按钮一】的回调
                                    AmCharts.makeChart("chartdiv", {
                                        "type": "serial",
                                        "dataProvider": chartDatas[0],
                                        "categoryField": "bus",
                                        "graphs": [
                                            {
                                                "valueField": "voltage",
                                                "type": "line",
                                                "fillAlphas": 0,
                                                //"angle": 30,
                                                //"depth3D": 15,
                                                "bullet": "round",
                                                "lineColor": "#8d1cc6",
                                                "balloonText": "[[category]]:<b>[[value]]</b>"
                                            }
                                        ],
                                        "allLabels": [
                                            {
                                                "text": "节点电压曲线",
                                                "align": "center",
                                                "bold": "true",
                                                "size": 20,
                                                "y": 10
                                            }
                                        ],
                                        "marginTop": 50
                                    });

                                    layer.open({
                                        type: 1 //Page层类型
                                        , area: ['1000px', '600px'], title: '', shade: 0.6 //遮罩透明度
                                        , maxmin: true //允许全屏最小化
                                        , anim: 0 //0-6的动画形式，-1不开启
                                        , content: $('#chartdiv'), moveOut: true
                                    });

                                });
                                $("button#1").click(function () {
                                    //按钮【按钮一】的回调
                                    AmCharts.makeChart("chartdiv", {
                                        "type": "serial",
                                        "dataProvider": chartDatas[1],
                                        "categoryField": "bus",
                                        "graphs": [
                                            {
                                                "valueField": "voltage",
                                                "type": "line",
                                                "fillAlphas": 0,
                                                //"angle": 30,
                                                //"depth3D": 15,
                                                "bullet": "round",
                                                "lineColor": "#8d1cc6",
                                                "balloonText": "[[category]]:<b>[[value]]</b>"
                                            }
                                        ],
                                        "allLabels": [
                                            {
                                                "text": "节点电压曲线",
                                                "align": "center",
                                                "bold": "true",
                                                "size": 20,
                                                "y": 10
                                            }
                                        ],
                                        "marginTop": 50
                                    });

                                    layer.open({
                                        type: 1 //Page层类型
                                        , area: ['1000px', '600px'], title: '', shade: 0.6 //遮罩透明度
                                        , maxmin: true //允许全屏最小化
                                        , anim: 0 //0-6的动画形式，-1不开启
                                        , content: $('#chartdiv'), moveOut: true
                                    });

                                });


                            }
                        }
                    });
            };

            vm.shishiJiance = function(){
                var postID = faultlineService.getID();
                var postSection = $('#'+postID);
                postSection.children("path").attr("stroke-width","0.4");
                postSection.children("path").attr("stroke","#00FF00");
                postSection.children("polyline").attr("stroke","#00FF00");
                postSection.children("polygon").attr("fill","#00FF00");
                var postID2 = "monixianlu1";
                postSection = $('#'+postID2);
                postSection.children("path").attr("stroke-width","0.4");
                postSection.children("path").attr("stroke","#00FF00");
                postSection.children("polyline").attr("stroke","#00FF00");
                postSection.children("polygon").attr("fill","#00FF00");
                window.clearInterval(faultlineService.gettimer());
                var timer = setInterval(function () {
                    vm.shishiJiance2();
                },50);
                faultlineService.settimer(timer);
            };
        }
    );
})();