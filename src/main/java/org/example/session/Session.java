
package org.example.session;

import org.example.dto.Member;

public class Session {
    public Member loginedMember;
    public int loginedMemberId;
    public boolean isLogined;

    public Session() {
        loginedMember = null;
        loginedMemberId = -1;
        isLogined = false;
    }
}