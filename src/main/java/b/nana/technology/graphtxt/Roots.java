package b.nana.technology.graphtxt;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Roots {

    private final Set<NodeTxt> roots;

    public Roots(Nodes nodes) {
        roots = new LinkedHashSet<>(nodes.values());
        roots.removeIf(root -> nodes.values().stream().anyMatch(n -> n.linksTo(root)));
        if (roots.isEmpty()) roots.add(nodes.values().iterator().next());
    }

    public void forEach(Consumer<NodeTxt> consumer) {
        roots.forEach(consumer);
    }
}
