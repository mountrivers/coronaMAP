package com.sanha.coronamap.CLASS;

public class User
{

    private String uid, email, name, profileUrl;
    private boolean selection;

    public User(){

    }

    /**
     * @brief 사용자의 정보를 저장하는 메서드
     * @param uid 사용자의 식별 id
     * @param email 사용자의 email 주소
     * @param name 사용자의 이름
     * @param profileUrl 사용자의 프로필url
     */

    public User(String uid, String email, String name, String profileUrl) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;
    }

    /**
     * @brief 사용자의 email을 저장하는 메서드
     * @param email 사용자의 email
     */

    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * @brief 사용자의 이름을 저장하는 메서드
     * @param name 사용자의 이름
     */

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @brief 사용자의 uid을 저장하는 메서드
     * @param uid 사용자의 uid
     */

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    /**
     * @brief 사용자의 프로필url을 저장하는 메서드
     * @param profileUrl 사용자의 프로필url
     */

    public void setProfileUrl(String profileUrl)
    {
        this.profileUrl = profileUrl;
    }

    /**
     * @brief 사용자의 email을 받아오는 메서드
     * @return 사용자의 email
     */

    public String getEmail()
    {
        return email;
    }

    /**
     * @brief 사용자의 이름을 받아오는 메서드
     * @return 사용자의 이름
     */

    public String getName()
    {
        return name;
    }

    /**
     * @brief 사용자의 uid를 받아오는 메서드
     * @return 사용자의 uid
     */

    public String getUid()
    {
        return uid;
    }

    /**
     * @brief 사용자의 프로필url을 받아오는 메서드
     * @return 사용자의 프로필url
     */

    public String getProfileUrl()
    {
        return profileUrl;
    }

    /**
     * @brief 사용자가 선택이 되었는지 아닌지 판별하는 메서드
     * @details 이 판별을 통하여 친구추가를 하거나 채팅방에 초대를 한다
     * @return selection
     */

    public boolean isSelection()
    {
        return selection;
    }

    /**
     * @brief 사용자 선택여부를 설정하는 메서드
     * @details 사용자 선택여부를 통하여 '친구추가'와 '채팅방 초대'에 사용된다
     * @param selection
     */

    public void setSelection(boolean selection)
    {
        this.selection = selection;
    }

//    public void toggleUserSelectStatus()
//    {
//        if(selection)
//            selection = false;
//        else
//            selection = true;
//    }
}
