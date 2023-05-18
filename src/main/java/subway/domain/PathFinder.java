package subway.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final List<Section> sections;

    public PathFinder(final List<Section> sections) {
        this.sections = sections;
    }

    public Path findShortestPath(final Station departure, final Station arrival) {
        final Station namedDeparture = addNameToStation(departure);
        final Station namedArrival = addNameToStation(arrival);
        final GraphPath<Station, DefaultWeightedEdge> shortestPath = getDijkstraShortestPath().getPath(namedDeparture, namedArrival);
        return new Path(shortestPath.getVertexList(), shortestPath.getWeight());
    }

    private Station addNameToStation(final Station station) {
        final Optional<Station> result = sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .filter(s -> s.equals(station))
                .findFirst();

        return result.orElseThrow(() -> new IllegalArgumentException("노선에 등록되지 않은 역입니다.: " + station.getId()));
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> getDijkstraShortestPath() {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        sections.forEach(section -> {
            final Station upStation = section.getUpStation();
            final Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance().getValue());
        });
        return new DijkstraShortestPath<>(graph);
    }
}
