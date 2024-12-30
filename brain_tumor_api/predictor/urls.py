from django.urls import path
from .views import PredictTumor

urlpatterns = [
    path('predict/', PredictTumor, name='predict'),  # Remove .as_view() for function-based views
]
