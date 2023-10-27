package mikufan.cx.nitterlb.model
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.net.URL
import java.time.ZonedDateTime

data class InstancesStatus (
  @JsonProperty("hosts")
  val instanceHosts: List<InstanceHost>,
  @JsonProperty("last_update")
  val lastUpdate: ZonedDateTime,
  @JsonProperty("latest_commit")
  val latestCommit: String
)

data class InstanceHost (
  val url: URL,
  val domain: String,
  @JsonProperty("points")
  val score: Int,
  val rss: Boolean,
  @JsonProperty("recent_pings")
  val recentPings: List<Int>,
  @JsonProperty("ping_max")
  val pingMax: Int,
  @JsonProperty("ping_min")
  val pingMin: Int,
  @JsonProperty("ping_avg")
  val pingAvg: Int,
  val version: String?,
  @JsonProperty("version_url")
  val versionUrl: String?,
  val healthy: Boolean,
  @JsonProperty("last_healthy")
  val lastHealthy: ZonedDateTime,
  @JsonProperty("is_upstream")
  val isUpstream: Boolean,
  @JsonProperty("is_latest_version")
  val isLatestVersion: Boolean,
  @JsonProperty("is_bad_host")
  val isBadHost: Boolean,
  val country: String,
  @JsonProperty("recent_checks")
  val recentChecks: List<RecentCheck>,
  @JsonProperty("healthy_percentage_overall")
  val healthyPercentageOverall: Int,
  val connectivity: String?,
  @JsonProperty("__show_last_seen")
  val showLastSeen: Boolean
)

@JsonFormat(shape=JsonFormat.Shape.ARRAY)
@JsonPropertyOrder("timestamp", "status")
data class RecentCheck (
  val timestamp: String,
  val status: Boolean
)