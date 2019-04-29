package paopao.util;

public enum EProgram {

    GROUP_SUSLIKS(1004, "susliks", "打地鼠",1000000,"qu1s6yNmc8cuE48j82gDzMaBJZlmHoHhIKdBCEGxI63I3HKCnQkM+QWz6YnjmWE9"),
    GROUP_RIVER(1005, "river", "小马过河",1000001,"9G+odzZ2HFQIIfEd4K2Si1Lj5yGB/rxVytdHlMb+j5vI3HKCnQkM+QWz6YnjmWE9"),
    GROUP_GASTRONOME(1006, "gastronome", "泡泡美食家",1000002,"dlof5gJc+fmuA7FY1XfE3BYP/qvGTGTY77U6Mzg4YPjI3HKCnQkM+QWz6YnjmWE9"),
    GROUP_REVERSI(1007, "reversi", "翻翻乐",1000003,"/3UlfHEsKMG8+zwJIBksdL2Vq+JQSARGRgFdLiOabHDI3HKCnQkM+QWz6YnjmWE9"),
    GROUP_ELIMINATELE(1008, "eliminatele", "泡泡消消乐",1000004,"TL6/gIj5LUgCR63s0I0qUL79Jet0wLznGTtB2HiBfAnI3HKCnQkM+QWz6YnjmWE9"),
    GROUP_JUMP(1009, "jump", "泡泡蹦一蹦",1000005,"vBFrTInwq+URrFbBLDgLS6lFSjBJcbStq4lonviX5h7I3HKCnQkM+QWz6YnjmWE9"),
    GROUP_BASKETBALL(1010, "basketball", "泡泡投篮机",1000006,"b52ZeL6gGIESeR5frRZD9iIoeNcoIaVJvuz0vMTDAXTI3HKCnQkM+QWz6YnjmWE9"),
    GROUP_PUSH(1011, "push", "泡泡比武场",1000007,"dsmVeXlAw+kvVZnSIaYWr4cHljZGTF1deqdjadL0tWTI3HKCnQkM+QWz6YnjmWE9"),
    GROUP_MORRA(1013, "morra", "泡泡猜拳",1000009,"dsmVeXlAw+kvVZnSIaYWr4cHljZGTF1deqdjadL0tWTI3HKCnQkM+QWz6YnjmWE9"),
    GROUP_CAMAIGN(1014, "camaign", "泡泡大作战",1000010,"dsmVeXlAw+kvVZnSIaYWr4cHljZGTF1deqdjadL0tWTI3HKCnQkM+QWz6YnjmWE9"),
    GROUP_CONTINUATIONLOOK(1016, "continuationLook", "连连看",1000012,"dsmVeXlAw+kvVZnSIaYWr4cHljZGTF1deqdjadL0tWTI3HKCnQkM+QWz6YnjmWE9");

    private final int gameId;
    private final String prefix;
    private final String desc;
    private final long userId;//服务器监控所需userId
    private final String token;//服务器监控所需userId
    
    EProgram(int gameId, String prefix, String desc, long userId, String token) {
        this.gameId = gameId;
        this.prefix = prefix;
        this.desc = desc;
        this.userId = userId;
        this.token = token;
    }
    
    public int getGameId() {
        return gameId;
    }

    public static EProgram getById(int gameId) {
        for (EProgram program : EProgram.values()) {
            if (program.getGameId() == gameId) {
                return program;
            }
        }
        return null;
    }
    
    public String getPrefix() {
        return prefix;
    }

    public String getDesc() {
        return desc;
    }

    public long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

}
