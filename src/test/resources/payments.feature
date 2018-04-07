Feature: Manage payments
  Background:
    And create new payment with name "createdPayment" with definition "newPayment.json"
  Scenario: Create payment
    Then find "createdPayment" as "findPayment"
    And last status is 200
    And "findPayment" is equals as "createdPayment"
  Scenario: Delete payment
    Then delete "createdPayment" as "deletedPayment"
    And find "createdPayment" as "findPayment"
    And last status is 404
  Scenario: Update payment
    Given update payment "createdPayment" with definition "updatePayment.json" as "updatedPayment"
    And last status is 200
    And find "createdPayment" as "findPayment"
    And last status is 200
    And "findPayment" is equals as "updatedPayment"
  Scenario: Find All payments
    When create new payment with name "createdPayment2" with definition "newPayment.json"
    And create new payment with name "createdPayment3" with definition "newPayment.json"
    Then get all payments as "allPayments"
    And payment "createdPayment" is in "allPayments"
    And payment "createdPayment2" is in "allPayments"
    And payment "createdPayment3" is in "allPayments"
   Scenario: Find non existing payment
    Given a payment with name "nonExistingPayment" with definition "nonExistingPayment.json"
    Then find "nonExistingPayment" as "findPayment"
    And last status is 404
   Scenario: Delete non existing payment
    Given a payment with name "nonExistingPayment" with definition "nonExistingPayment.json"
    Then delete "nonExistingPayment" as "deletedPayment"
    And last status is 404
  Scenario: Update non existing payment
    Given a payment with name "nonExistingPayment" with definition "nonExistingPayment.json"
    Then update payment "nonExistingPayment" with definition "updatePayment.json" as "updatedPayment"
    And last status is 200
