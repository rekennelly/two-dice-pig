package com.kennelra.twodicepig

import android.content.Context
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    // Store dice faces in an array
    val die = arrayOf(R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6)
    var playerTurn = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize all TextViews
        var player1Total : TextView = findViewById(R.id.player1Total)
        var player2Total : TextView = findViewById(R.id.player2Total)
        var turnTotal : TextView = findViewById(R.id.turnTotal)
        var playerTurnLabel : TextView = findViewById(R.id.playerTurnLabel)
        var holdButton : Button = findViewById(R.id.holdButton)
        var rollButton : Button = findViewById(R.id.rollButton)

        // Set all scores to zero
        player1Total.text = "0"
        player2Total.text = "0"
        turnTotal.text = "0"

        // Start with player 1's turn
        playerTurnLabel.text = getString(R.string.player1_turn)

    } //end onCreate

    // roll the dice function
    fun rollDice(view : View) {
        // Reinitialize hold button in case it was disabled in roll before
        var holdButton : Button = findViewById(R.id.holdButton)
        holdButton.isClickable = true
        holdButton.isEnabled = true
        holdButton.setBackgroundColor(getColor(R.color.pink))
        holdButton.setTextColor(getColor(R.color.maroon))

        // Initialize all score TextViews
        var player1Total : TextView = findViewById(R.id.player1Total)
        var player2Total : TextView = findViewById(R.id.player2Total)
        var turnTotal : TextView = findViewById(R.id.turnTotal)
        var playerTurnLabel : TextView = findViewById(R.id.playerTurnLabel)

        // Determine whose turn it is
        if (playerTurnLabel.text == getString(R.string.player1_turn)) {
            playerTurn = 1
        } else {
            playerTurn = 2
        }

        // Generate random int between 0-5
        var roll1 = (0..5).random()
        var roll2 = (0..5).random()

        // Set dice to die[roll] where roll = 0 is equivalent to rolling a 1
        val firstDice : ImageView = findViewById(R.id.firstDice)
        val secondDice : ImageView = findViewById(R.id.secondDice)
        firstDice.setImageResource(die[roll1])
        secondDice.setImageResource(die[roll2])
        // Set content description for accessibility
        when (roll1) {
            0 -> firstDice.setContentDescription(getString(R.string.dice1_contentdescription))
            1 -> firstDice.setContentDescription(getString(R.string.dice2_contentdescription))
            2 -> firstDice.setContentDescription(getString(R.string.dice3_contentdescription))
            3 -> firstDice.setContentDescription(getString(R.string.dice4_contentdescription))
            4 -> firstDice.setContentDescription(getString(R.string.dice5_contentdescription))
            5 -> firstDice.setContentDescription(getString(R.string.dice6_contentdescription))
        }
        when (roll2) {
            0 -> secondDice.setContentDescription(getString(R.string.dice1_contentdescription))
            1 -> secondDice.setContentDescription(getString(R.string.dice2_contentdescription))
            2 -> secondDice.setContentDescription(getString(R.string.dice3_contentdescription))
            3 -> secondDice.setContentDescription(getString(R.string.dice4_contentdescription))
            4 -> secondDice.setContentDescription(getString(R.string.dice5_contentdescription))
            5 -> secondDice.setContentDescription(getString(R.string.dice6_contentdescription))
        }

        // Add 1 to each roll to make the array index match the value on the dice
        roll1++
        roll2++

        // CASE: Snake Eyes (aka double ones)
        if (roll1 == 1 && roll2 == 1) {
            // Set score to 0
            turnTotal.text = "0"
            when (playerTurn) {
                1 -> player1Total.text = "0"
                2 -> player2Total.text = "0"
            }
        }
        // CASE: player rolled a one
        // We know if the function reaches this if statement that it is only a singular one
        if (roll1 == 1 || roll2 == 1) {
            // Score nothing
            turnTotal.text = "0"
            // Next player's turn
            if (playerTurn == 1) {
                playerTurn = 2
                playerTurnLabel.text = getString(R.string.player2_turn)
            } else {
                playerTurn = 1
                playerTurnLabel.text = getString(R.string.player1_turn)
            }
        }
        // CASE: player rolled no ones
        else {
            // Update turn total
            var turnTotalInt = roll1 + roll2
            turnTotal.text = turnTotalInt.toString()

            // If roll1 = roll2, hide hold button
            if (roll1 == roll2) {
                // Make button unusable & hide in the background to preserve alignment
                hideButton(holdButton)
            }
        }
    }//end rollDice

    // hold user's turn
    fun holdTurn(view : View) {
        // Initialize all score TextViews & buttons
        var player1Total : TextView = findViewById(R.id.player1Total)
        var player2Total : TextView = findViewById(R.id.player2Total)
        var turnTotal : TextView = findViewById(R.id.turnTotal)
        var playerTurnLabel : TextView = findViewById(R.id.playerTurnLabel)
        var rollButton : Button = findViewById(R.id.rollButton)
        var holdButton : Button = findViewById(R.id.holdButton)

        // Get int value of turn total
        var turnTotalString = turnTotal.text.toString()
        var turnTotalInt = turnTotalString.toInt()
        // If it's player1's turn, add to player1's score
        if (playerTurn == 1) {
            // Convert score string to int for calculations
            var player1TotalString = player1Total.text.toString()
            var player1TotalInt = player1TotalString.toInt();

            player1TotalInt += turnTotalInt

            // Update score field with new score
            player1Total.text = player1TotalInt.toString()

            // See if new score made player win
            if (player1TotalInt >= 50) {
                playerTurnLabel.text = getString(R.string.player1_victory)
                // Make buttons disappear to end the game
                hideButton(holdButton)
                hideButton(rollButton)
            } else {
                // Make it the next player's turn
                playerTurn = 2
                playerTurnLabel.text = getString(R.string.player2_turn)
            }
        }
        else {
            // Convert score string to int for calculations
            var player2TotalString = player2Total.getText().toString()
            var player2TotalInt = player2TotalString.toInt();

            player2TotalInt += turnTotalInt

            // Update score field with new score
            player2Total.text = player2TotalInt.toString()

            // See if score update made player win
            if (player2TotalInt >= 50) {
                playerTurnLabel.text = getString(R.string.player2_victory)
                // Make buttons disappear to end the game
                hideButton(holdButton)
                hideButton(rollButton)
            } else {
                // Make it the next player's turn
                playerTurn = 1
                playerTurnLabel.text = getString(R.string.player1_turn)
            }
        }
    }//end holdTurn

    fun hideButton(button : Button) {
        button.isClickable = false
        button.isEnabled = false
        button.setBackgroundColor(getColor(R.color.coral))
        button.setTextColor(getColor(R.color.coral))
    }

}//end MainActivity