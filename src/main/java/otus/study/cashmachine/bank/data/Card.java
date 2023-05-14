package otus.study.cashmachine.bank.data;

public class Card {
    long id;

    private Long accountId;
    private String number;
    private String pinHash;

    public Card(final long id, final String number, final Long accountId, final String pinHash) {
        this.id = id;
        this.number = number;
        this.accountId = accountId;
        this.pinHash = pinHash;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(final Long accountId) {
        this.accountId = accountId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(final String pinHash) {
        this.pinHash = pinHash;
    }
}
