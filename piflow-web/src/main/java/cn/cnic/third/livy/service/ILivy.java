package cn.cnic.third.livy.service;

import java.util.Map;

public interface ILivy {

    /**
     * getAllSessions
     *
     * @return
     */
    public Map<String, Object> getAllSessions();

    /**
     * startSessions
     *
     * @return
     */
    public Map<String, Object> startSessions();

    /**
     * stopFlow
     *
     * @param sessionsId
     * @return
     */
    public Map<String, Object> stopSessions(String sessionsId);

    /**
     * stopSessionsState
     *
     * @param sessionsId
     * @return
     */
    public Map<String, Object> getSessionsState(String sessionsId);
    
    /**
     * getFlowProgress
     *
     * @param sessionsId
     * @param code
     * @return
     */
    public Map<String, Object> runStatements(String sessionsId, String code);

    /**
     * getStatementsResult
     *
     * @param sessionsId
     * @param statementsId
     * @return
     */
    public Map<String, Object> getStatementsResult(String sessionsId, String statementsId);

}
