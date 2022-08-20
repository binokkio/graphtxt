This is a low quality codebase written specifically to render plain text visualizations of flows in Gingester.

Example code:

new GraphTxt(List.of(
    SimpleNode.of("Aaaaa", "Bbbb", "C", "F"),
    SimpleNode.of("Bbbb", "F"),
    SimpleNode.of("C"),
    SimpleNode.of("D", "Bbbb", "E", "ZZZ", "H"),
    SimpleNode.of("E"),
    SimpleNode.of("F"),
    SimpleNode.of("ZZZ"),
    SimpleNode.of("H", "Iiii"),
    SimpleNode.of("Iiii")
)).getText()

Will produce:

  ┌───────┐       ┌───┐
  │ Aaaaa │       │ D │
  └───┬───┘       └─┬─┘
      │             │
  ┌───┴───┐         │
  │       ├──────┬──┴───┬──────┐
  │       │      │      │      │
┌─┴─┐ ┌───┴──┐ ┌─┴─┐ ┌──┴──┐ ┌─┴─┐
│ C │ │ Bbbb │ │ E │ │ ZZZ │ │ H │
└───┘ └───┬──┘ └───┘ └─────┘ └─┬─┘
          │                    │
          │                    │
          │                   ┌┘
          │                   │
        ┌─┴─┐             ┌───┴──┐
        │ F │             │ Iiii │
        └───┘             └──────┘

The layout algorithm keeps the nodes within the horizontal space used by the widest row.

Edges that skip a row, e.g. Aaaaa-F in the example, currently draw behind other edges and nodes which needs to
be improved.