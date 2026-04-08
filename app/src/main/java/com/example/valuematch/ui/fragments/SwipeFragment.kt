package com.example.valuematch.ui.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.valuematch.R
import com.example.valuematch.data.models.SwipeCard
import com.example.valuematch.databinding.FragmentSwipeBinding
import com.example.valuematch.repository.SwipeRepository
import com.example.valuematch.ui.adapters.SwipeCardAdapter
import kotlinx.coroutines.launch
import kotlin.math.abs

class SwipeFragment : Fragment(R.layout.fragment_swipe) {

    private var _binding: FragmentSwipeBinding? = null
    private val binding get() = _binding!!

    private val repo = SwipeRepository()
    private lateinit var adapter: SwipeCardAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSwipeBinding.bind(view)

        adapter = SwipeCardAdapter(mutableListOf())

        binding.cardStack.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically() = false
        }
        binding.cardStack.adapter = adapter

        attachSwipe()

        binding.btnNope.setOnClickListener {
            val top = adapter.peekTop() ?: run {
                updateEmptyState()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                repo.passUser(top.userId)
                adapter.removeTop()
                adapter.clearStamps()
                updateEmptyState()
            }
        }

        binding.btnLike.setOnClickListener {
            val top = adapter.peekTop() ?: run {
                updateEmptyState()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                repo.likeUser(top.userId)
                adapter.removeTop()
                adapter.clearStamps()
                updateEmptyState()
            }
        }

        binding.btnRefresh.setOnClickListener {
            loadUsers()
        }

        loadUsers()
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            val users = repo.loadSwipeCandidates()

            val cards = users.map { u ->
                val valuesLine =
                    if (u.values.isNotEmpty()) u.values.take(3).joinToString(" • ")
                    else u.about

                SwipeCard(
                    userId = u.userId,
                    name = u.name.ifBlank { "User" },
                    age = u.age,
                    bioLine = valuesLine,
                    photoUrl = u.photoUrl
                )
            }

            if (_binding == null) return@launch

            adapter.submit(cards)
            adapter.clearStamps()
            updateEmptyState()
        }
    }

    private fun updateEmptyState() {
        val currentBinding = _binding ?: return
        val isEmpty = adapter.itemCount == 0

        currentBinding.emptyState.isVisible = isEmpty
        currentBinding.cardStack.isGone = isEmpty

        currentBinding.btnLike.isEnabled = !isEmpty
        currentBinding.btnNope.isEnabled = !isEmpty

        currentBinding.btnLike.alpha = if (isEmpty) 0.35f else 1f
        currentBinding.btnNope.alpha = if (isEmpty) 0.35f else 1f
    }

    private fun attachSwipe() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val removed = adapter.removeTop() ?: run {
                    updateEmptyState()
                    return
                }

                lifecycleScope.launch {
                    if (dir == ItemTouchHelper.RIGHT) {
                        repo.likeUser(removed.userId)
                    } else {
                        repo.passUser(removed.userId)
                    }

                    if (_binding == null) return@launch

                    adapter.clearStamps()
                    updateEmptyState()
                }
            }

            override fun onChildDraw(
                c: Canvas,
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                state: Int,
                active: Boolean
            ) {
                super.onChildDraw(c, rv, vh, dX, dY, state, active)

                val width = rv.width.toFloat().coerceAtLeast(1f)
                val p = (dX / width).coerceIn(-1f, 1f)

                adapter.setSwipeProgress(p)

                vh.itemView.rotation = 12f * p
                val s = 1f - (abs(p) * 0.04f)
                vh.itemView.scaleX = s
                vh.itemView.scaleY = s
            }
        }).attachToRecyclerView(binding.cardStack)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}