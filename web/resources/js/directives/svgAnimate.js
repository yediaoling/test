/**
 * Created by yediaoling on 2016/4/14.
 */

(function () {
    appPW.directive('svgAnimate', ['$compile','$location',function ($compile,$location) {
        var selectFlag = 0;
        var viewBox = '';


        function autosize(svgEle, siblingEle, parentEle){

            rowWidth = siblingEle.width();
            rowHeight = siblingEle.height();

            documentHeight = $(document).height();
            documentWidth = $(document).width();

            viewContainerHeight = rowHeight;

            viewContainerWidth = documentWidth - rowWidth;

            svgEleWidth = svgEle.getAttribute('width');
            svgEleHeight = svgEle.getAttribute('height');

            parentEle.height(viewContainerHeight);


            svgEle.setAttribute('height', viewContainerHeight);
            svgEle.setAttribute('width', viewContainerWidth);
        }

        function zoom(zoomType) {

            var svg_js = document.getElementsByTagName('svg')[0];
            viewBox = svg_js.getAttribute('viewBox');

            var viewBoxX = viewBox.split(' ')[0];
            var viewBoxY = viewBox.split(' ')[1];
            var viewBoxWidth = viewBox.split(' ')[2];
            var viewBoxHeight = viewBox.split(' ')[3];

            zoomRate = 1.1;
            if (zoomType == 'zoomIn') {

                viewBoxWidth /= zoomRate;	// Decrease the width and height attributes of the viewBox attribute to zoom in.
                viewBoxHeight /= zoomRate;
                //alert( viewBoxWidth+'1')
            }
            else if (zoomType == 'zoomOut') {
                viewBoxWidth *= zoomRate;	// Increase the width and height attributes of the viewBox attribute to zoom out.
                viewBoxHeight *= zoomRate;
                //alert( viewBoxWidth+'2')
            }
            else
                alert("function zoom(zoomType) given invalid zoomType parameter.");

            //svgRoot.setAttribute('viewBox', viewBoxValues.join(' '));	// Convert the viewBoxValues array into a string with a white space character between the given values.


            try {
                viewBox = viewBoxX + ' ' + viewBoxY + ' ' + viewBoxWidth + ' ' + viewBoxHeight;
            }
            catch (exp) {
                alert('error')
            }
            svg_js.setAttribute('viewBox', viewBox);
        }


        function zoomViaMouseWheel(mouseWheelEvent) {
            if (mouseWheelEvent.wheelDelta > 0)
                zoom('zoomIn');
            else
                zoom('zoomOut');

            /* When the mouse is over the webpage, don't let the mouse wheel scroll the entire webpage: */
            mouseWheelEvent.cancelBubble = true;
            return false;
        }


        function selectElement(evt) {
            selectFlag = 1;
            selectedElement = $('svg');
            currentX = evt.clientX;
            currentY = evt.clientY;
        }

        function moveElement(evt) {
            if (selectFlag == 1) {

                var dx = evt.clientX - currentX;
                var dy = evt.clientY - currentY;


                var svg_js = document.getElementsByTagName('svg')[0];
                viewBox = svg_js.getAttribute('viewBox');

                var viewBoxX = viewBox.split(' ')[0];
                var viewBoxY = viewBox.split(' ')[1];
                var viewBoxWidth = viewBox.split(' ')[2];
                var viewBoxHeight = viewBox.split(' ')[3];


                viewBoxX -= dx;
                viewBoxY -= dy;

                try {
                    viewBox = viewBoxX + ' ' + viewBoxY + ' ' + viewBoxWidth + ' ' + viewBoxHeight;
                }
                catch (exp) {
                    alert('error')
                }
                svg_js.setAttribute('viewBox', viewBox);

                currentX = evt.clientX;
                currentY = evt.clientY;

            }

        }

        function deselectElement(evt) {
            selectFlag = 0;
        }


        return {
            restrict: 'A'
            ,link: function( scope, element, attrs ) {
                var svgEle = $(element).find('svg')[0];
                var siblingEle = $('div#sidebar');
                var parentEle = $('#view-container');
                autosize(svgEle, siblingEle, parentEle);

                svgEle.addEventListener('mousedown',selectElement);
                svgEle.addEventListener('mouseup',deselectElement);
                svgEle.addEventListener('mousemove',moveElement);
                svgEle.addEventListener('mousewheel',zoomViaMouseWheel);
            }
        }

    }]);
})();
