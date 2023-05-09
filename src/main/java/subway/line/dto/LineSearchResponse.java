package subway.line.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.domain.Station;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineSearchResponse {

    private Long id;
    private String name;
    private String color;
    private List<Station> stations;
}
