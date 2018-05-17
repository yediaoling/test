/**
 * Created by yediaoling on 2016/4/14.
 */

(function () {
    appPW.directive('autoheight', ['$compile','$location',function ($compile,$location) {
        function autoHeight(ft, hd, sd) {
            $(document).ready(function() {
                documentHeight = $(document).height();
                documentWidth = $(document).width();

                headerHeight = hd.height();
                headerFooter = ft.height();

                rowHeight = documentHeight - headerHeight -headerFooter;
                rowWidth = sd.width();

                sd.height(rowHeight);
            });

        }
        return {
            restrict: 'A'
            ,link: function(scope, element, attrs) {
                var ft, hd, sd;
                hd = $('div#header');
                ft = $('div#footer');
                sd = $('div#sidebar');
                autoHeight(ft, hd, sd);
            }

        }

    }])
})();