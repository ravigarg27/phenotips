/**
 * Class for organizing graphics for PlaceHolder nodes.
 *
 * @class PlaceHolderVisuals
 * @extends AbstractPersonVisuals
 * @constructor
 * @param {PlaceHolder} node The node for which the graphics are handled
 * @param {Number} x The x coordinate on the canvas
 * @param {Number} y The y coordinate on the canvas
 */

var PlaceHolderVisuals = Class.create(AbstractPersonVisuals, {

    initialize: function($super, node, x, y) {
        $super(node, x, y);
        //this.setDraggable();
    },

    /**
     * Sets/replaces the gender symbol with the symbol appropriate for the gender. Returns raphael set containing
     * the genderShape, a shadow behind it, and the text "drag me or click me".
     *
     * @method setGenderSymbol
     */
    setGenderSymbol: function($super) {
        $super();
        var shape = this.getGenderSymbol().attr(PedigreeEditor.attributes.phShape);
        var text = editor.getPaper().text(this.getX(), this.getY() - PedigreeEditor.attributes.radius/20, editor.DEBUG_MODE ? "" : "CLICK ME \nOR \nDRAG ME");
        text.attr(PedigreeEditor.attributes.dragMeLabel);
        shape.push(text);
        shape.attr("cursor", "pointer");
        shape.ox = shape.getBBox().x;
        shape.oy = shape.getBBox().y;
        var layerBelow = (this._highlightBox) ? this._highlightBox : editor.getGraph().getProband().getGraphics().getAllGraphics().flatten()
        shape.flatten().insertAfter(layerBelow);
        this._icon = this._genderSymbol = shape;
        this.setDraggable();
    },

    /**
     * Handles the dragging and clicking behavior of the placeholder
     *
     * @method setDraggable
     */
    setDraggable: function() {
        var me = this,
            isDragged,
            draggable = true,
            ox = 0,
            oy = 0,
            absOx,
            absOy;

        //Action on mouse click
        var start = function() {
            if(!me.isAnimating) {
                absOx = me.getX();
                absOy = me.getY();
                isDragged = false;
                me.getShapes().toFront();
                editor.getGraph().setCurrentDraggable(me.getNode());
                editor.getGraph().enterHoverMode(me.getNode(), ["Person"]);
            }
        };

        //Called when the placeholder is dragged
        var move = function(dx, dy) {
            dx = dx/editor.getWorkspace().zoomCoefficient;
            dy = dy/editor.getWorkspace().zoomCoefficient;
            if(!me.isAnimating) {
                me.setPos(absOx + dx, absOy + dy);
                ox = dx;
                oy = dy;
                if(dx > 2 || dx < -2 || dy > 2 || dy < -2 ) {
                    isDragged = true;
                }
            }
        };

        //Called when the mouse button is released
        var end = function() {
            if(!me.isAnimating){
                if(isDragged) {
                    draggable = false;
                    var node = me.getNode().validHoveredNode;
                    me.getNode().validHoveredNode = null;
                    if(node) {
                        me.getNode().mergeAction(node, absOx, absOy);
                    }
                    else {
                        me.isAnimating = true;
                        me.setPos(absOx, absOy, true, function() {
                            me.isAnimating = false;
                        });
                        ox = 0;
                        oy = 0;
                    }
                }
                else {
                    me.setPos(absOx, absOy, false);
                    if(me.getNode().getGender() == "U") {
                        var x = me.getX(),
                            y = me.getY() + PedigreeEditor.attributes.radius,
                            position = editor.getWorkspace().canvasToDiv(x, y);
                        editor.getNodetypeSelectionBubble().show(me.getNode(), position.x, position.y);
                    }
                    else {
                        me.getNode().createNodeAction("Person", me.getNode().getGender());
                    }
                }
                editor.getGraph().exitHoverMode();
                editor.getGraph().setCurrentDraggable(null);
            }
        };
        //Adds dragging capability to the genderSymbols
        me.getGenderSymbol().drag(move, start, end);
    }
});