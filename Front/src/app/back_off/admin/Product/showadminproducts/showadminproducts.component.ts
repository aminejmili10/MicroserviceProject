import { Component, OnInit, OnDestroy } from '@angular/core';
import { ProductService, Product } from '../../../../services/ProductService/ProductService';
import { Subscription } from 'rxjs';
import { ChartData, ChartOptions } from 'chart.js';

@Component({
  selector: 'app-showadminproducts',
  templateUrl: './showadminproducts.component.html',
  styleUrls: ['./showadminproducts.component.css']
})
export class ShowadminproductsComponent implements OnInit, OnDestroy {
  products: Product[] = [];
  selectedCategory: string | null = null;
  categories: string[] = [];
  private subscription: Subscription = new Subscription();
  errorMessage: string = '';
  showStatistics: boolean = false;
  barChartData: ChartData<'bar'> = { labels: [], datasets: [] };
  barChartOptions: ChartOptions = {
    responsive: true,
    scales: {
      y: {
        beginAtZero: true,
        title: { display: true, text: 'Number of Products' }
      },
      x: {
        title: { display: true, text: 'Category' }
      }
    },
    plugins: {
      legend: { display: false }
    }
  };

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.subscription = this.productService.products$.subscribe({
      next: (products) => {
        this.products = products;
        this.updateCategories();
        this.updateStatistics();
      },
      error: (err) => console.error('Error subscribing to products:', err)
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  filterByCategory(category: string): void {
    this.selectedCategory = category;
  }

  isActiveCategory(category: string): boolean {
    return this.selectedCategory === category;
  }

  get filteredProducts(): Product[] {
    return this.selectedCategory
      ? this.products.filter(product => product.category === this.selectedCategory)
      : this.products;
  }

  getProductImage(image: string): string {
    return image && image.startsWith('http') ? image : 'assets/images/default-product.jpg';
  }

  deleteProduct(id: number | undefined): void {
    if (id === undefined) {
      this.errorMessage = 'Cannot delete product: Invalid ID';
      return;
    }
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          console.log('Product deleted:', id);
          this.errorMessage = '';
          this.updateStatistics();
        },
        error: (err) => {
          console.error('Error deleting product:', err);
          this.errorMessage = 'Failed to delete product: ' + (err.error?.message || err.message || 'Unknown error');
        }
      });
    }
  }

  private updateCategories(): void {
    this.categories = [...new Set(this.products.map(product => product.category))];
    if (!this.selectedCategory && this.categories.length > 0) {
      this.selectedCategory = this.categories[0];
    } else if (this.selectedCategory && !this.categories.includes(this.selectedCategory)) {
      this.selectedCategory = null;
    }
  }

  toggleStatistics(): void {
    this.showStatistics = !this.showStatistics;
  }

  private updateStatistics(): void {
    const statsMap = this.products.reduce((acc, product) => {
      acc[product.category] = (acc[product.category] || 0) + 1;
      return acc;
    }, {} as { [key: string]: number });

    const labels = Object.keys(statsMap);
    const data = Object.values(statsMap);

    this.barChartData = {
      labels: labels,
      datasets: [
        {
          label: 'Products per Category',
          data: data,
          backgroundColor: 'rgba(0, 123, 255, 0.5)',
          borderColor: 'rgba(0, 123, 255, 1)',
          borderWidth: 1
        }
      ]
    };
  }
}
