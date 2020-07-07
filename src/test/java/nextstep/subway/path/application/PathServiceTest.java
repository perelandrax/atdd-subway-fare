package nextstep.subway.path.application;

import com.google.common.collect.Lists;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @Mock
    private LineService lineService;
    @Mock
    private GraphService graphService;

    private PathService pathService;

    private List<LineResponse> lines;
    private PathResult pathResult;

    @BeforeEach
    void setUp() {
        StationResponse stationResponse1 = new StationResponse(1L, "교대역", LocalDateTime.now(), LocalDateTime.now());
        StationResponse stationResponse2 = new StationResponse(2L, "강남역", LocalDateTime.now(), LocalDateTime.now());
        StationResponse stationResponse3 = new StationResponse(3L, "양재역", LocalDateTime.now(), LocalDateTime.now());
        StationResponse stationResponse4 = new StationResponse(4L, "남부터미널역", LocalDateTime.now(), LocalDateTime.now());

        LineStationResponse lineStationResponse1 = new LineStationResponse(stationResponse1, null, 2, 2);
        LineStationResponse lineStationResponse2 = new LineStationResponse(stationResponse2, 1L, 2, 2);

        LineStationResponse lineStationResponse3 = new LineStationResponse(stationResponse2, null, 2, 2);
        LineStationResponse lineStationResponse4 = new LineStationResponse(stationResponse3, 2L, 2, 1);

        LineStationResponse lineStationResponse5 = new LineStationResponse(stationResponse1, null, 2, 2);
        LineStationResponse lineStationResponse6 = new LineStationResponse(stationResponse4, 1L, 1, 2);
        LineStationResponse lineStationResponse7 = new LineStationResponse(stationResponse3, 4L, 2, 2);

        LineResponse lineDetailResponse1 = new LineResponse(1L, "2호선", "GREEN", LocalTime.now(), LocalTime.now(), 5, Lists.newArrayList(lineStationResponse1, lineStationResponse2), LocalDateTime.now(), LocalDateTime.now());
        LineResponse lineDetailResponse2 = new LineResponse(2L, "신분당선", "RED", LocalTime.now(), LocalTime.now(), 5, Lists.newArrayList(lineStationResponse3, lineStationResponse4), LocalDateTime.now(), LocalDateTime.now());
        LineResponse lineDetailResponse3 = new LineResponse(3L, "3호선", "ORANGE", LocalTime.now(), LocalTime.now(), 5, Lists.newArrayList(lineStationResponse5, lineStationResponse6, lineStationResponse7), LocalDateTime.now(), LocalDateTime.now());

        lines = Lists.newArrayList(lineDetailResponse1, lineDetailResponse2, lineDetailResponse3);
        pathResult = new PathResult(Lists.newArrayList(1L, 4L, 3L), 3);

        pathService = new PathService(lineService, graphService);
    }

    @Test
    void findPath() {
        when(lineService.findAllLinesWithStations()).thenReturn(lines);
        when(graphService.findPath(anyList(), anyLong(), anyLong(), any())).thenReturn(pathResult);

        PathResponse pathResponse = pathService.findPath(1L, 3L, PathType.DISTANCE);

        assertThat(pathResponse.getStations()).isNotEmpty();
        assertThat(pathResponse.getDuration()).isNotZero();
        assertThat(pathResponse.getDistance()).isNotZero();
    }
}
