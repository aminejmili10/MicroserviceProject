import { Component, OnInit, OnDestroy } from '@angular/core';
import { ProductService, Product } from '../../../services/ProductService/ProductService';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-show-client-product',
  templateUrl: './show-client-product.component.html',
  styleUrls: ['./show-client-product.component.css']
})
export class ShowClientProductComponent implements OnInit, OnDestroy {
  products: Product[] = [];
  filteredProducts: Product[] = [];
  selectedCategory: string | null = null;
  categories: string[] = [];
  errorMessage: string = '';
  private subscription: Subscription = new Subscription();

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.subscription = this.productService.products$.subscribe({
      next: (products) => {
        this.products = products.filter(p => p.id !== undefined);
        this.filteredProducts = this.products;
        this.updateCategories();
        console.log('Client products loaded:', this.products);
      },
      error: (err) => {
        console.error('Error loading products:', err);
        this.errorMessage = 'Failed to load products. Please try again later.';
      }
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  filterByCategory(category: string): void {
    this.selectedCategory = category;
    this.filteredProducts = this.selectedCategory
      ? this.products.filter(product => product.category === this.selectedCategory)
      : this.products;
  }

  isActiveCategory(category: string): boolean {
    return this.selectedCategory === category;
  }

  getProductImage(image: string): string {
    return image && image.startsWith('http') ? image : 'assets/images/default-product.jpg';
  }

  private updateCategories(): void {
    this.categories = [...new Set(this.products.map(product => product.category))];
    if (!this.selectedCategory && this.categories.length > 0) {
      this.selectedCategory = null; // Show all products by default
    }
  }
}
