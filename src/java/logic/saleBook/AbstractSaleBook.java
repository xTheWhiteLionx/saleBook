package logic.saleBook;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * This abstract class presents a comment SaleBook
 *
 * @author xthe_white_lionx
 */
public abstract class AbstractSaleBook {

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
     * Constructor for an AbstractSaleBook
     */
    public AbstractSaleBook() {
        this.repairServiceSales = BigDecimal.ZERO;
        this.extraordinaryIncome = BigDecimal.ZERO;
        this.paid = BigDecimal.ZERO;
        this.fixedCosts = BigDecimal.ZERO;
    }


    /**
     * Constructor for a AbstractSaleBook with the specified parameters
     *
     * @param repairServiceSales the sales of the repair services
     * @param extraordinaryIncome extraordinary income, which does not influence the sales
     * @param paid the number of paid taxes
     * @param fixedCosts the total number of fixed costs
     * @throws IllegalArgumentException if the specified nextPosId or the specified nextOrderId is less than 0
     */
    public AbstractSaleBook(@NotNull BigDecimal repairServiceSales,
                            @NotNull BigDecimal extraordinaryIncome, @NotNull BigDecimal paid,
                            @NotNull BigDecimal fixedCosts) {
        this.repairServiceSales = repairServiceSales;
        this.extraordinaryIncome = extraordinaryIncome;
        this.paid = paid;
        this.fixedCosts = fixedCosts;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractSaleBook abstractSaleBook)) {
            return false;
        }
        return Objects.equals(this.repairServiceSales, abstractSaleBook.repairServiceSales) &&
                Objects.equals(this.extraordinaryIncome, abstractSaleBook.extraordinaryIncome) &&
                Objects.equals(this.paid, abstractSaleBook.paid) &&
                Objects.equals(this.fixedCosts, abstractSaleBook.fixedCosts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.repairServiceSales, this.extraordinaryIncome, this.paid,
                this.fixedCosts);
    }

    @Override
    public String toString() {
        return "AbstractSaleBook{" +
               "repairServiceSales=" + this.repairServiceSales +
               ", extraordinaryIncome=" + this.extraordinaryIncome +
               ", paid=" + this.paid +
               ", fixedCosts=" + this.fixedCosts +
               '}';
    }
}