This is a low quality codebase written specifically to render plain text visualizations of flows in Gingester.

Example code:

new GraphTxt(List.of(
    SimpleNode.of("AAAAA", "BBBB", "C", "F"),
    SimpleNode.of("BBBB", "F"),
    SimpleNode.of("C"),
    SimpleNode.of("D", "BBBB", "E", "ZZZ", "H"),
    SimpleNode.of("E"),
    SimpleNode.of("F"),
    SimpleNode.of("ZZZ"),
    SimpleNode.of("H", "IIII"),
    SimpleNode.of("IIII")
)).getText()

Will produce:

  ┌───────┐       ┌───┐
  │ AAAAA │       │ D │
  └───┬───┘       └─┬─┘
      │             │
  ┌───┴───┐         │
  │       ├──────┬──┴───┬──────┐
  │       │      │      │      │
┌─┴─┐ ┌───┴──┐ ┌─┴─┐ ┌──┴──┐ ┌─┴─┐
│ C │ │ BBBB │ │ E │ │ ZZZ │ │ H │
└───┘ └───┬──┘ └───┘ └─────┘ └─┬─┘
          │                    │
          │                    │
          │                   ┌┘
          │                   │
        ┌─┴─┐             ┌───┴──┐
        │ F │             │ IIII │
        └───┘             └──────┘

The layout algorithm keeps the nodes within the horizontal space used by the widest row.

Edges that skip a row, e.g. AAAAA-F in the example, currently draw behind other edges and nodes which needs to
be improved.