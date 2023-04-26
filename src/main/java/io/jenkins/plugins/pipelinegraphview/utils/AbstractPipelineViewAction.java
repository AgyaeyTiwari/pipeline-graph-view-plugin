package io.jenkins.plugins.pipelinegraphview.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hudson.model.Action;
import hudson.model.BallColor;
import hudson.security.Permission;
import hudson.util.HttpResponses;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;
import org.jenkins.ui.icon.IconSpec;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.WebMethod;

public abstract class AbstractPipelineViewAction implements Action, IconSpec {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(AbstractPipelineViewAction.class.getName());

    protected final transient PipelineGraphApi api;
    protected final transient WorkflowRun run;

    public AbstractPipelineViewAction(WorkflowRun target) {
        this.api = new PipelineGraphApi(target);
        this.run = target;
    }

    public boolean isBuildable() {
        return run.getParent().isBuildable();
    }

    public Permission getPermission() {
        return run.getParent().BUILD;
    }

    public Permission getConfigurePermission() {
        return run.getParent().CONFIGURE;
    }

    public String getBuildDisplayName() {
        return run.getDisplayName();
    }

    public String getFullBuildDisplayName() {
        return run.getFullDisplayName();
    }

    public String getFullProjectDisplayName() {
        return run.getParent().getFullDisplayName();
    }

    private String getBuildNumber(WorkflowRun run) {
        if (run != null) {
            return String.valueOf(run.getNumber());
        }
        return null;
    }

    public String getPreviousBuildNumber() {
        return getBuildNumber(run.getPreviousBuild());
    }

    public String getNextBuildNumber() {
        return getBuildNumber(run.getNextBuild());
    }

    public BallColor getIconColor() {
        return run.getIconColor();
    }

    public String getBuildStatusIconClassName() {
        return run.getBuildStatusIconClassName();
    }

    protected JSONObject createJson(PipelineGraph pipelineGraph) throws JsonProcessingException {
        String graph = OBJECT_MAPPER.writeValueAsString(pipelineGraph);
        graph = "{\"stages\":[{\"name\":\"pre-parallel\",\"children\":[],\"state\":\"SUCCESS\",\"completePercent\":50,\"type\":\"STAGE\",\"title\":\"pre-parallel\",\"id\":\"4\",\"seqContainerName\":null,\"nextSibling\":null,\"synthetic\":false,\"pauseDurationMillis\":\"Queued 0 ms\",\"startTimeMillis\":\"Started 4 sec ago\",\"totalDurationMillis\":\"Took 50 ms\",\"isSequential\":false},{\"name\":\"Parallel\",\"children\":[{\"name\":\"p1\",\"children\":[{\"name\":\"p1-0\",\"children\":[],\"state\":\"SUCCESS\",\"completePercent\":50,\"type\":\"STAGE\",\"title\":\"p1-0\",\"id\":\"13\",\"seqContainerName\":null,\"nextSibling\":null,\"synthetic\":false,\"pauseDurationMillis\":\"Queued 0 ms\",\"startTimeMillis\":\"Started 3.8 sec ago\",\"totalDurationMillis\":\"Took 58 ms\",\"isSequential\":false},{\"name\":\"parallel\",\"children\":[{\"name\":\"p1-1-1\",\"children\":[],\"state\":\"SUCCESS\",\"completePercent\":50,\"type\":\"PARALLEL\",\"title\":\"p1-1-1\",\"id\":\"24\",\"seqContainerName\":null,\"nextSibling\":null,\"synthetic\":false,\"pauseDurationMillis\":\"Queued -157 ms\",\"startTimeMillis\":\"Started 3.7 sec ago\",\"totalDurationMillis\":\"Took -157 ms\",\"isSequential\":false},{\"name\":\"p1-1-2\",\"children\":[],\"state\":\"SUCCESS\",\"completePercent\":50,\"type\":\"PARALLEL\",\"title\":\"p1-1-2\",\"id\":\"25\",\"seqContainerName\":null,\"nextSibling\":null,\"synthetic\":false,\"pauseDurationMillis\":\"Queued -163 ms\",\"startTimeMillis\":\"Started 3.7 sec ago\",\"totalDurationMillis\":\"Took -163 ms\",\"isSequential\":false},],\"state\":\"SUCCESS\",\"completePercent\":50,\"type\":\"STAGE\",\"title\":\"parallel\",\"id\":\"411\",\"seqContainerName\":null,\"nextSibling\":null,\"synthetic\":false,\"pauseDurationMillis\":\"Queued 0 ms\",\"startTimeMillis\":\"Started 3.4 sec ago\",\"totalDurationMillis\":\"Took 34 ms\",\"isSequential\":false},{\"name\":\"p1-2\",\"children\":[],\"state\":\"SUCCESS\",\"completePercent\":50,\"type\":\"STAGE\",\"title\":\"p1-2\",\"id\":\"41\",\"seqContainerName\":null,\"nextSibling\":null,\"synthetic\":false,\"pauseDurationMillis\":\"Queued 0 ms\",\"startTimeMillis\":\"Started 3.4 sec ago\",\"totalDurationMillis\":\"Took 34 ms\",\"isSequential\":false}],\"state\":\"SUCCESS\",\"completePercent\":50,\"type\":\"PARALLEL\",\"title\":\"p1\",\"id\":\"10\",\"seqContainerName\":null,\"nextSibling\":null,\"synthetic\":false,\"pauseDurationMillis\":\"Queued 0 ms\",\"startTimeMillis\":\"Started 3.9 sec ago\",\"totalDurationMillis\":\"Took 48 ms\",\"isSequential\":false},{\"name\":\"p2\",\"children\":[],\"state\":\"SUCCESS\",\"completePercent\":50,\"type\":\"PARALLEL\",\"title\":\"p2\",\"id\":\"11\",\"seqContainerName\":null,\"nextSibling\":null,\"synthetic\":false,\"pauseDurationMillis\":\"Queued 0 ms\",\"startTimeMillis\":\"Started 3.9 sec ago\",\"totalDurationMillis\":\"Took 41 ms\",\"isSequential\":false}],\"state\":\"SUCCESS\",\"completePercent\":50,\"type\":\"STAGE\",\"title\":\"Parallel\",\"id\":\"8\",\"seqContainerName\":null,\"nextSibling\":null,\"synthetic\":false,\"pauseDurationMillis\":\"Queued -320 ms\",\"startTimeMillis\":\"Started 7 ms ago\",\"totalDurationMillis\":\"Took -231 ms\",\"isSequential\":false},{\"name\":\"post-parallel\",\"children\":[],\"state\":\"SUCCESS\",\"completePercent\":50,\"type\":\"STAGE\",\"title\":\"post-parallel\",\"id\":\"48\",\"seqContainerName\":null,\"nextSibling\":null,\"synthetic\":false,\"pauseDurationMillis\":\"Queued 0 ms\",\"startTimeMillis\":\"Started 3.3 sec ago\",\"totalDurationMillis\":\"Took 33 ms\",\"isSequential\":false}],\"complete\":true}";
        return JSONObject.fromObject(graph);
    }

    @WebMethod(name = "tree")
    public HttpResponse getTree() throws JsonProcessingException {
        JSONObject graph = createJson(api.createTree());

        return HttpResponses.okJSON(graph);
    }

    @WebMethod(name = "replay")
    public HttpResponse replayRun(StaplerRequest req) {

        JSONObject result = new JSONObject();

        Integer estimatedNextBuildNumber;
        try {
            estimatedNextBuildNumber = api.replay();
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            LOGGER.log(Level.SEVERE, "Failed to queue item", e);
            return HttpResponses.errorJSON("failed to queue item: " + e.getMessage());
        }

        if (estimatedNextBuildNumber == null) {
            return HttpResponses.errorJSON("failed to get next build number");
        }

        result.put(
                "url",
                appendTrailingSlashIfRequired(req.getContextPath())
                        + run.getUrl().replace("/" + run.getNumber() + "/", "/" + estimatedNextBuildNumber + "/")
                        + "pipeline-graph/");

        result.put("success", true);
        return HttpResponses.okJSON(result);
    }

    private static String appendTrailingSlashIfRequired(String string) {
        if (string.endsWith("/")) {
            return string;
        }

        return string + "/";
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getIconClassName() {
        return "symbol-file-tray-stacked-outline plugin-ionicons-api";
    }
}
