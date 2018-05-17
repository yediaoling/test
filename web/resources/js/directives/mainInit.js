/**
 * Created by yediaoling on 2016/4/14.
 */

(function () {
    appPW.directive('mainInit', ['$compile','$location',function ($compile,$location) {

        return {
            restrict: 'A'
            ,link: function(scope, element, attrs) {
                $(document).ready(function () {


                    $('.menu a').click(function( ) {

                        $('.menu a').removeClass('active');

                        var $this = $(this);
                        if (!$this.hasClass('active')) {
                            $this.addClass('active');
                        }
//                        console.log($this.attr('name'))
                        if ($this.attr('id') == 'svgpfdetail/jichuanxian' || $this.attr('id') == 'svgpfdetail/*'){
                            $('div #mainbody').css("background-color", 'black');
                        }else {
                            $('div #mainbody').css("background-color", 'aliceblue');
                        }

                    });
                });
            }

        }

    }])
})();