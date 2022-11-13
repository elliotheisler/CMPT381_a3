Built with Maven. run from EditorApp.

I keep model and view concerns seperate. Model knows nothing about the view at any point.


PARTIALLY IMPLEMENTED / BUGS:
    - Creating links from a node to itself:
        - rendered as a two-way arrow, not a circle
        - link box gets placed right on top of the node

    - Mini view:
        - no rendering of arrows
        - nodes/links location and size are scaled down by the right amount,
          but have weird and different offsets
        - the semi-transparent overlay is the correct size though,
          and updates with window resizing according to min(width, height)
          - need to pan to update it though

    - Panning:
        - not restricted to inside the world, but i added a big rect to show the boundary

    - Arrows:
     - render in wrong direction when dragging rectangles on top each other


OTHER NOTES TO THE MARKER:
- PropertiesView: NodePropertiesView and LinkPropertiesView are implemented in one class

- spaghetti code in the AppController could have been avoided if I used the "state pattern" instead
  of enum-based switch statements.