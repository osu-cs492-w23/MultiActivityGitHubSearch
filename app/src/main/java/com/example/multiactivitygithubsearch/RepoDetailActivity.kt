package com.example.multiactivitygithubsearch

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.multiactivitygithubsearch.data.GitHubRepo
import com.google.android.material.snackbar.Snackbar

const val EXTRA_GITHUB_REPO = "GITHUB_REPO"

class RepoDetailActivity : AppCompatActivity() {
    private var repo: GitHubRepo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_detail)

        if (intent != null && intent.hasExtra(EXTRA_GITHUB_REPO)) {
            repo = intent.getSerializableExtra(EXTRA_GITHUB_REPO) as GitHubRepo

            findViewById<TextView>(R.id.tv_repo_name).text = repo!!.name
            findViewById<TextView>(R.id.tv_repo_stars).text = repo!!.stars.toString()
            findViewById<TextView>(R.id.tv_repo_description).text = repo!!.description
        }
    }

    private fun shareRepo() {
        if (repo != null) {
            val shareText = getString(R.string.share_text, repo!!.name, repo!!.url)
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(Intent.createChooser(intent, null))
        }
    }

    private fun viewOnWeb() {
        if (repo != null) {
            val uri = Uri.parse(repo!!.url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Snackbar.make(
                    findViewById(R.id.coordinator),
                    "No app installed to handle that activity.  Please install a web browser.",
                    Snackbar.LENGTH_LONG
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.repo_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_view_on_web -> {
                viewOnWeb()
                true
            }
            R.id.action_share -> {
                shareRepo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}