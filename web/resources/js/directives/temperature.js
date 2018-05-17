/**
 * Created by yediaoling on 2017/3/10.
 */

(function () {
    appPW.directive('temperature', ['$compile','$location','$http',function ($compile,$location,$http) {

        return {
            restrict: 'A'
            ,link: function(scope, element, attrs) {

                require.config({
                    paths: {
                        echarts: 'bower_components/echarts-2.2.7/echarts-2.2.7/build/dist'
                    }
                });
                // 使用
                require(
                    [
                        'echarts'
                        ,'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
                        , 'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
//                        ,'echarts/chart/k' // 使用柱状图就加载bar模块，按需加载
                    ],
                    function (ec) {
                        // 基于准备好的dom，初始化echarts图表
                        var myChart = ec.init(element[0]);
                        var temperature_url = "temperatureInfo";
                        var temperatureDate;
                        var option;
                        readDB(temperature_url);

                        function readDB(url) {
                            $http.get(url,
                                {
                                    params: {
                                        dbMode: encodeURI("read")
                                    }
                                }).
                                success(function (data, status, headers, config) {

                                    temperatureDate = data;
                                    day = [];
                                    maxtemp = [];
                                    mintemp = [];
                                    angular.forEach(temperatureDate, function(val,key){
                                        day.push(val.day);
                                        val.maxtemp.replace('MW','');
                                        maxtemp.push(parseInt(val.maxtemp));
                                        val.mintemp.replace('MW','');
                                        mintemp.push(parseInt(val.mintemp));
                                    });
//                                    console.log(data);
                                    option = {
                                        title: {
                                            text: '实时负荷预测'
                                        },
                                        legend: {
                                            data: ['最大负荷','最小负荷']
                                        },
                                        toolbox: {
                                            show: true,
                                            feature: {
                                                mark: {show: true},
                                                dataZoom: {show: true},
                                                dataView: {show: true, readOnly: false},
                                                magicType: {show: true, type: ['line', 'bar']},
                                                restore: {show: true},
                                                saveAsImage: {show: true}
                                            }
                                        },
                                        dataZoom: {
                                            show: true,
                                            realtime: true,
                                            start: 50,
                                            end: 100
                                        },
                                        xAxis: [
                                            {
                                                type: 'category',
                                                boundaryGap: true,
                                                axisTick: {onGap: false},
                                                splitLine: {show: false},
                                                data: day
                                            }
                                        ],
                                        yAxis: [
                                            {
                                                type: 'value',
                                                scale: true,
                                                splitNumber: 5,
                                                boundaryGap: [0.01, 0.01]
                                            },
                                            {
                                                type: 'value',
                                                scale: true,
                                                splitNumber: 5,
                                                boundaryGap: [0.05, 0.05],
                                                axisLabel: {
                                                    formatter: function (v) {
                                                        return Math.round(v / 10000) + ' 万'
                                                    }
                                                }
                                            }
                                        ],
                                        series: [
                                            {
                                                name: '最高气温',
                                                type: 'line',
//                                                yAxisIndex: 1,
                                                symbol: 'none',
                                                data: maxtemp,
                                                markPoint: {
                                                    symbol: 'emptyPin',
                                                    itemStyle: {
                                                        normal: {
                                                            color: '#1e90ff',
                                                            label: {
                                                                show: true,
                                                                position: 'top',
                                                                formatter: function (param) {
                                                                    return Math.round(param.value) + 'MW'
                                                                }
                                                            }
                                                        }
                                                    },
                                                    data: [
                                                        {type: 'max', name: '最大值', symbolSize: 5},
                                                        {type: 'min', name: '最小值', symbolSize: 5}
                                                    ]
                                                },
                                                markLine: {
                                                    symbol: 'none',
                                                    itemStyle: {
                                                        normal: {
                                                            color: '#1e90ff',
                                                            label: {
                                                                show: true,
                                                                formatter: function (param) {
                                                                    return Math.round(param.value) + 'MW'
                                                                }
                                                            }
                                                        }
                                                    },
                                                    data: [
                                                        {type: 'average', name: '平均值'}
                                                    ]
                                                }
                                            },
                                            {
                                                name: '最低气温',
                                                type: 'line',
//                                                yAxisIndex: 2,
                                                symbol: 'none',
                                                data: mintemp,
                                                markPoint: {
                                                    symbol: 'emptyPin',
                                                    itemStyle: {
                                                        normal: {
                                                            color: 'green',
                                                            label: {
                                                                show: true,
                                                                position: 'top',
                                                                formatter: function (param) {
                                                                    return Math.round(param.value) + 'MW'
                                                                }
                                                            }
                                                        }
                                                    },
                                                    data: [
                                                        {type: 'max', name: '最大值', symbolSize: 5},
                                                        {type: 'min', name: '最小值', symbolSize: 5}
                                                    ]
                                                },
                                                markLine: {
                                                    symbol: 'none',
                                                    itemStyle: {
                                                        normal: {
                                                            color: '#1e90ff',
                                                            label: {
                                                                show: true,
                                                                formatter: function (param) {
                                                                    return Math.round(param.value) + 'MW'
                                                                }
                                                            }
                                                        }
                                                    },
                                                    data: [
                                                        {type: 'average', name: '平均值'}
                                                    ]
                                                }
                                            }

                                        ]
                                    };
                                    // 为echarts对象加载数据

                                    myChart.setOption(option);

                                });
                        }
                    }
                );

            }
        }
    }])
})();