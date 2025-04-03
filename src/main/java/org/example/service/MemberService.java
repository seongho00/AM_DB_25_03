package org.example.service;

import org.example.container.Container;
import org.example.dao.MemberDao;

import java.util.Map;

public class MemberService {

    private MemberDao memberDao;

    public MemberService() {
        this.memberDao = Container.memberDao;
    }

    public boolean isLoginIdDup(String loginId) {
        return memberDao.isLoginIdDup(loginId);
    }

    public int doJoin(String loginId, String loginPw, String name) {
        return memberDao.doJoin(loginId, loginPw, name);
    }

    public Map<String, Object> getLoginId(String loginId) {
        return memberDao.getLoginId(loginId);
    }
}