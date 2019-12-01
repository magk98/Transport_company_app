package model;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CurrentTransaction {
    private UUID _id;
    private Transaction transaction;
    private Map<String, Integer> cargoLeft;

    public CurrentTransaction(Transaction transaction, Map<String, Integer> cargoLeft) {
        this._id = UUID.randomUUID();
        this.transaction = transaction;
        this.cargoLeft = cargoLeft;
    }

    public UUID get_id() {
        return _id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Map<String, Integer> getCargoLeft() {
        return cargoLeft;
    }

    public void setCargoLeft(Map<String, Integer> cargoLeft) {
        this.cargoLeft = cargoLeft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrentTransaction)) return false;
        CurrentTransaction that = (CurrentTransaction) o;
        return get_id().equals(that.get_id()) &&
                Objects.equals(getTransaction(), that.getTransaction()) &&
                Objects.equals(getCargoLeft(), that.getCargoLeft());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get_id(), getTransaction(), getCargoLeft());
    }
}
