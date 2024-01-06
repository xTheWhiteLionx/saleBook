package logic.saleBook;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author xthe_white_lionx
 */
public abstract class SaleBookImpl {

    /**
     * The repair service sales of this saleBookImpl
     */
    protected BigDecimal repairServiceSales;

    /**
     * The extraordinary income, which does not influence the sales
     */
    protected BigDecimal extraordinaryIncome;

    /**
     * The amount of money of paid taxes
     */
    protected BigDecimal paid;

    /**
     * The fixed cost of this saleBookImpl
     */
    protected BigDecimal fixedCosts;

    /**
     * the id for the next creation of a position
     */
    protected int nextPosId;

    /**
     * the id for the next creation of an order
     */
    protected int nextOrderId;

    /**
     * Constructor for an SaleBookImpl
     */
    public SaleBookImpl() {
        this.repairServiceSales = BigDecimal.ZERO;
        this.extraordinaryIncome = BigDecimal.ZERO;
        this.paid = BigDecimal.ZERO;
        this.fixedCosts = BigDecimal.ZERO;
        this.nextPosId = 1;
        this.nextOrderId = 1;
    }


    /**
     * Constructor for a SaleBookImpl with the specified parameters
     *
     * @param repairServiceSales the sales of the repair services
     * @param extraordinaryIncome extraordinary income, which does not influence the sales
     * @param paid the number of paid taxes
     * @param fixedCosts the total number of fixed costs
     * @param nextPosId the id for the next creation of a position
     * @param nextOrderId the id for the next creation of an order
     * @throws IllegalArgumentException if the specified nextPosId or the specified nextOrderId is less than 0
     */
    public SaleBookImpl(@NotNull BigDecimal repairServiceSales,
                        @NotNull BigDecimal extraordinaryIncome, @NotNull BigDecimal paid,
                        @NotNull BigDecimal fixedCosts, int nextPosId, int nextOrderId) {
        if (nextPosId < 1) {
            throw new IllegalArgumentException("nextPosId must be greater equals 1 but is " +
                    nextPosId);
        }

        if (nextOrderId < 1) {
            throw new IllegalArgumentException("nextOrderId must be greater equals 1 but is " +
                    nextOrderId);
        }

        this.repairServiceSales = repairServiceSales;
        this.extraordinaryIncome = extraordinaryIncome;
        this.paid = paid;
        this.fixedCosts = fixedCosts;
        this.nextPosId = nextPosId;
        this.nextOrderId = nextOrderId;
    }

    /**
     * Returns the sales of the repair service of this saleBookImpl
     *
     * @return the sales of the repair service of this saleBookImpl
     */
    public @NotNull BigDecimal getRepairServiceSales() {
        return this.repairServiceSales;
    }

    /**
     * Returns the extraordinary income of this saleBookImpl
     *
     * @return the extraordinary income of this saleBookImpl
     */
    public @NotNull BigDecimal getExtraordinaryIncome() {
        return this.extraordinaryIncome;
    }

    /**
     * Returns the amount of money of paid taxes of this saleBookImpl
     *
     * @return the amount of money of paid taxes of this saleBookImpl
     */
    public @NotNull BigDecimal getPaid() {
        return this.paid;
    }

    /**
     * Returns the fixed costs of this saleBookImpl
     *
     * @return the fixed costs of this saleBookImpl
     */
    public @NotNull BigDecimal getFixedCosts() {
        return this.fixedCosts;
    }

    /**
     * Returns the id for the next creation of a position
     *
     * @return the id for the next creation of a position
     */
    public int getNextPosId() {
        return this.nextPosId;
    }

    /***
     * Returns the id for the next creation of an order
     *
     * @return the id for the next creation of an order
     */
    public int getNextOrderId() {
        return this.nextOrderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaleBookImpl saleBook)) {
            return false;
        }
        return this.nextPosId == saleBook.nextPosId && this.nextOrderId == saleBook.nextOrderId &&
                Objects.equals(this.repairServiceSales, saleBook.repairServiceSales) &&
                Objects.equals(this.extraordinaryIncome, saleBook.extraordinaryIncome) &&
                Objects.equals(this.paid, saleBook.paid) &&
                Objects.equals(this.fixedCosts, saleBook.fixedCosts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.repairServiceSales, this.extraordinaryIncome, this.paid, this.fixedCosts,
                this.nextPosId, this.nextOrderId);
    }

    @Override
    public String toString() {
        return "SaleBookImpl{" +
                " repairServiceSales=" + this.repairServiceSales +
                ", extraordinaryIncome=" + this.extraordinaryIncome +
                ", paid=" + this.paid +
                ", fixedCosts=" + this.fixedCosts +
                ", nextPosId=" + this.nextPosId +
                ", nextOrderId=" + this.nextOrderId +
                '}';
    }
}