# Round-Up-For-Savings
Takes all the transactions in a given week for a customer and then rounds them up to the nearest pound.

The main class is RoundUp which retrieves the account information for a customer. The account information is then used to retrieve the transaction feed API.

The Transaction class contains the key information of a transaction needed to calculate the round-ups. The key information is the feedItemUID, the transaction direction, the status of the transaction and the amount of the transaction.

The WeeklySavings class calculates the amount to be deducted from the account added into the savings goal. For a transaction, it finds whether the transaction direction is "OUT", if the status of the transaction is "SETTLED" and whether the transaction was settled in the current week. If all these conditions are satisfied, then the round-up is found and summed together.
