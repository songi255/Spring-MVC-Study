package detail.session;

import detail.spring.Member;
import detail.spring.MemberDao;
import detail.spring.WrongIdPasswordException;

public class AuthService {
    // 로그인을 처리해서 AuthInfo 를 생성해주는 서비스
    private MemberDao memberDao;

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public AuthInfo authenticate(String email, String password){
        Member member = memberDao.selectByEmail(email);
        if (member == null){
            throw new WrongIdPasswordException();
        }
        if (!member.matchPassword(password)){
            throw new WrongIdPasswordException();
        }
        return new AuthInfo(member.getId(),
                member.getEmail(),
                member.getName());
    }
}
