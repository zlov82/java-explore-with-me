package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.models.Hit;
import ru.practicum.models.StatShortResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {

    @Query("""
            SELECT app as app, uri as uri, COUNT(DISTINCT ip) AS views FROM Hit
            WHERE 1=1
            AND uri IN :uris
            AND (localDateTime >= :start AND localDateTime < :end)
            GROUP BY app, uri
            ORDER BY views DESC
            """)
    List<StatShortResponse> selectUniquesStat(@Param("uris") List<String> uris,
                                              @Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

    @Query("""
            SELECT app as app, uri as uri, COUNT(DISTINCT ip) AS views FROM Hit
            WHERE 1=1
            AND (localDateTime >= :start AND localDateTime < :end)
            GROUP BY app, uri
            ORDER BY views DESC
            """)
    List<StatShortResponse> selectUniquesStat(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);


    @Query("""
            SELECT app as app, uri as uri, COUNT(ip) AS views FROM Hit
            WHERE 1=1
            AND uri IN :uris
            AND (localDateTime >= :start AND localDateTime < :end)
            GROUP BY app, uri
            ORDER BY views DESC
            """)
    List<StatShortResponse> selectNonUniquesStat(@Param("uris") List<String> uris,
                                                 @Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end);

    @Query("""
            SELECT app as app, uri as uri, COUNT(ip) AS views FROM Hit
            WHERE 1=1
            AND (localDateTime >= :start AND localDateTime < :end)
            GROUP BY app, uri
            ORDER BY views DESC
            """)
    List<StatShortResponse> selectNonUniquesStat(@Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end);
}
