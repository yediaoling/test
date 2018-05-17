/**
 * Created by yediaoling on 2016/11/11.
 */

(function () {
    appPW.controller(
        'fenduanCtrl',function($scope,$http,ngTableParams) {
            var vm= this;
            vm.title = 'fenduan.html';
            var url = "ResilienceModel";

            require.config({
                paths: {
                    echarts: 'bower_components/echarts-2.2.7/echarts-2.2.7/build/dist'
                }
            });
            require(
                [
                    'echarts'
                    ,'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
                ],
                function (ec) {
                    // 基于准备好的dom，初始化echarts图表
                    var myChart = ec.init(document.getElementById('diaodu'));
                    var option = {
                        title:{
                            text:'分布式出力优化调度对比图',
                            x:'center',
                            y:'bottom',
                            top:30,
                            textStyle:{
                                fontSize:25
                            }
                        },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        legend: {
                            data:['主网','光伏发电','风力发电','储能装置'],
                            textStyle:{
                                fontSize:15
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data : ['周一','周二','周三','周四','周五','周六','周日']
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value'
                            }
                        ],
                        series : [
                            {
                                name:'主网',
                                type:'bar',
                                stack: '原方案',
                                data:[1062, 1018, 1064, 1026, 1679, 1600, 1570]
                            },
                            {
                                name:'光伏发电',
                                type:'bar',
                                stack: '原方案',
                                data:[1020, 1132, 1001, 1034, 1090, 1130, 1120]
                            },
                            {
                                name:'风力发电',
                                type:'bar',
                                stack: '原方案',
                                data:[520, 532, 501, 534, 590, 430, 420]
                            },
                            {
                                name:'储能装置',
                                type:'bar',
                                stack: '原方案',
                                data:[60, 72, 71, 74, 190, 130, 110]
                            },
                            {
                                name:'主网',
                                type:'bar',
                                stack: '优化方案三',
                                data:[862, 1018, 964, 1026, 1279, 1400, 1270]
                            },
                            {
                                name:'光伏发电',
                                type:'bar',
                                stack: '优化方案三',
                                data:[620, 732, 701, 734, 1090, 1230, 1320]
                            },
                            {
                                name:'风力发电',
                                type:'bar',
                                stack: '优化方案三',
                                data:[120, 132, 101, 134, 290, 230, 220]
                            },
                            {
                                name:'储能装置',
                                type:'bar',
                                stack: '优化方案三',
                                data:[60, 72, 71, 74, 190, 130, 110]
                            },
                            {
                                name:'主网',
                                type:'bar',
                                stack: '优化方案二',
                                data:[462, 418, 464, 426, 479, 500, 470]
                            },
                            {
                                name:'光伏发电',
                                type:'bar',
                                stack: '优化方案二',
                                data:[520, 532, 501, 534, 690, 530, 520]
                            },
                            {
                                name:'风力发电',
                                type:'bar',
                                stack: '优化方案二',
                                data:[350, 362, 331, 364, 540, 470, 450]
                            },
                            {
                                name:'储能装置',
                                type:'bar',
                                stack: '优化方案二',
                                data:[450, 462, 451, 384, 440, 570, 650]
                            },
                            {
                                name:'主网',
                                type:'bar',
                                stack: '优化方案一',
                                data:[320, 332, 301, 334, 390, 330, 320]
                            },
                            {
                                name:'光伏发电',
                                type:'bar',
                                stack: '优化方案一',
                                data:[220, 232, 201, 234, 190, 230, 210]
                            },
                            {
                                name:'风力发电',
                                type:'bar',
                                stack: '优化方案一',
                                data:[220, 182, 191, 234, 290, 330, 310]
                            },
                            {
                                name:'储能装置',
                                type:'bar',
                                stack: '优化方案一',
                                data:[150, 232, 201, 154, 190, 330, 410]
                            }
                        ]
                    };
                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
                }
            );
        }
    );
})();